package ru.mvrlrd.playlistmaker.player.ui


import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import kotlin.properties.Delegates

class PlayerService : Service() {
    private val binder = LocalBinder()
    private val interactor: PlayerInteractor by inject()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null

    private var notificationManager: NotificationManager? = null

    var progressJob : Job? = null
    var currentTimeProgress: Int = 0

    var playerStateStateFlow = interactor.getLivePlayerState()
    var trackFlow = MutableStateFlow<PlayerTrack?>(null)

    private lateinit var track: PlayerTrack

    private val _curr = MutableStateFlow(-1L)
    val curr get() = _curr.asStateFlow()


    override fun onCreate() {
        super.onCreate()
        coroutineScope.launch {
            playerStateStateFlow.filter {
                (it == MyMediaPlayer.PlayerState.PAUSED) ||
                        (it == MyMediaPlayer.PlayerState.PLAYING) ||
                        (it == MyMediaPlayer.PlayerState.COMPLETED)
            }.collect() {
                val status = if (it == MyMediaPlayer.PlayerState.PAUSED) {
                    Action.PAUSE
                } else if ((it == MyMediaPlayer.PlayerState.PLAYING)) {
                    Action.PLAY
                } else {
                    Action.COMPLETED
               }
                createNotificationChannel()
                startForeground(NOTIFICATION_ID, createNotification(status = status, trackFlow.value!!))
            }

        }
    }

    private fun cancelNotification() {
        notificationManager?.cancel(NOTIFICATION_ID)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        trackFlow.value = intent?.getSerializableExtra(TRACK) as PlayerTrack
        track = intent.getSerializableExtra(TRACK) as PlayerTrack
        if (curr.value != track.trackId) {
            interactor.onDestroy()
            currentTimeProgress = 0
            progressJob?.cancel()
            job = coroutineScope.launch {
                interactor.prp(track)
            }
        } else {
            handlePlaying()
        }
        _curr.value = track.trackId


        return super.onStartCommand(intent, flags, startId)
    }

    fun handlePlaying(){
        interactor.handleStartAndPause()
    }


    override fun onDestroy() {
        super.onDestroy()
        cancelNotification()
    }

    inner class LocalBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "test Channel"
            }
            // Register the channel with the system
            notificationManager =
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                    createNotificationChannel(channel)
                }
        }
    }
    private fun createNotification(status: Action, track: PlayerTrack) : Notification {
        createNotificationChannel()
        var pendingIntentFlag by Delegates.notNull<Int>()
        pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val builder = notificationWithMediaControl(status, track)
        return builder.build()
}

    private fun notificationWithMediaControl(status: Action, track: PlayerTrack): NotificationCompat.Builder {
        val stopPlayIntent = Intent().apply {
            action = ACTION
        }
        val playStopButtonText = when(status){
            Action.PLAY,Action.NEXT_TRACK,Action.PREVIOUS_TRACK-> PAUSE_TEXT
            Action.PAUSE, Action.COMPLETED -> PLAY_TEXT
        }

        val pausePendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext, 0, stopPlayIntent, 0)
        val prevPendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext, 0, stopPlayIntent, 0)
        val nextPendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext, 0, stopPlayIntent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_share_icon)
            .addAction(R.drawable.ic_arrow_icon, "Previous", prevPendingIntent) // #0
            .addAction(R.drawable.baseline_pause_24, playStopButtonText, pausePendingIntent) // #1
            .addAction(R.drawable.baseline_favorite_full, "Next", nextPendingIntent) // #2
            .setSilent(true)
            .setContentTitle("${track.trackName}")
            .setContentText("${track.artistName}")

        val trackTime = TRACK_TIME
        val onePieceOfProgress = trackTime / 100L

        val PROGRESS_MAX = onePieceOfProgress.times(100).toInt()

        NotificationManagerCompat.from(this).apply {
            builder.setProgress(PROGRESS_MAX, currentTimeProgress, false)
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
            }
            if (status == Action.PLAY) {
                progressJob =  coroutineScope.launch {
                    repeat(COUNT_OF_PROGRESS_INCREASINGS) {
                        currentTimeProgress += (PROGRESS_MAX/ COUNT_OF_PROGRESS_INCREASINGS)
                        builder.setProgress(PROGRESS_MAX, currentTimeProgress, false)
                        notify(NOTIFICATION_ID, builder.build())
                        delay((PROGRESS_MAX/ COUNT_OF_PROGRESS_INCREASINGS).toLong())
                    }
                }
            }else if (status == Action.COMPLETED){
                currentTimeProgress = 0
                builder
                    .setProgress(PROGRESS_MAX, currentTimeProgress, false)
                notify(NOTIFICATION_ID, builder.build())
            }

            else{
                progressJob?.cancel()
                builder.setProgress(PROGRESS_MAX, currentTimeProgress, false)
            }
        }
        return builder
    }


    companion object {
        private const val TAG = "PlayerService"
        const val TRACK = "track"
        const val ACTION = "rururu"
        private const val CHANNEL_NAME = "channel"
        private const val CHANNEL_ID = "1"
        private const val NOTIFICATION_ID = 1
        private const val PLAY_TEXT = "PLAY"
        private const val PAUSE_TEXT = "PAUSE"
        private const val TRACK_TIME = 30000L //поменять если будут песни не по 30 сек
        private const val COUNT_OF_PROGRESS_INCREASINGS = 50

        fun newIntent(context: Context, track: PlayerTrack): Intent {
            return Intent(context, PlayerService::class.java).apply {
                putExtra(TRACK, track)
            }
        }
    }

    enum class Action {
        PLAY,
        PAUSE,
        COMPLETED,
        NEXT_TRACK,
        PREVIOUS_TRACK
    }
}
