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
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.main.MainActivity
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

    var ddd = interactor.getLivePlayerState()
    var trackFlow = MutableStateFlow<PlayerTrack?>(null)

    private lateinit var track: PlayerTrack

    private val _curr = MutableStateFlow(-1L)
    val curr get() = _curr.asStateFlow()


    override fun onCreate() {
        super.onCreate()
        coroutineScope.launch {
            ddd.filter { (it == MyMediaPlayer.PlayerState.PAUSED) || (it == MyMediaPlayer.PlayerState.PLAYING) || (it == MyMediaPlayer.PlayerState.COMPLETED)}.collect(){
               val status = if (it == MyMediaPlayer.PlayerState.PAUSED){
                   PLAY
                }else if ((it == MyMediaPlayer.PlayerState.PLAYING)) {
                    PAUSE
                }else{
                    PLAY
               }
                createNotificationChannel()
                startForeground(NOTIFICATION_ID, createNotification(status = status,trackFlow.value!!))
            }

        }
    }

    private fun cancelNotification() {
        notificationManager?.cancel(NOTIFICATION_ID)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        trackFlow.value = intent?.getSerializableExtra(TRACK) as PlayerTrack
        track = intent?.getSerializableExtra(TRACK) as PlayerTrack
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
    private fun createNotification(status: String, track: PlayerTrack) : Notification {
        createNotificationChannel()

        var pendingIntentFlag by Delegates.notNull<Int>()
        pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        createPendingIntentForNotification()

        createSnoozPendIntent()



        //        // Get the layouts to use in the custom notification
//        val notificationLayout = RemoteViews(packageName, ru.mvrlrd.playlistmaker.R.layout.notification)
////        val notificationLayoutExpanded = RemoteViews(packageName, R.layout.notification_large)
//
//      val icon =  if (status == PLAY){
//          R.drawable.baseline_play_arrow_24
//        }else{
//          R.drawable.baseline_pause_24
//        }
//
//
//        val remoteViews = RemoteViews(packageName, R.layout.notification_layout).apply {
//            setTextViewText(R.id.notification_tv_track_name, track.trackName)
//            setTextViewText(R.id.notification_tv_artist_name, track.artistName)
//            setImageViewResource(R.id.notification_album_image, icon)
//            setOnClickPendingIntent(R.id.root, createSnoozPendIntent())
//        }
//
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.baseline_add_photo_alternate_24)
//
//        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContent(remoteViews)
//            .setSilent(true)
//            .setLargeIcon(bitmap)
////            .setStyle(NotificationCompat.BigPictureStyle()
////                .bigPicture(bitmap)
////                .bigLargeIcon(bitmap))
////            .addAction(R.drawable.baseline_play_arrow_24, status,
////            createSnoozPendIntent())


//        return builder.build()


        val builder = notificationWithMediaControl(status, track)
//      val builder = createNotificationWithProgressBar(track)

        return builder.build()



//
//        return NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_agreement_icon)
//            .setContentTitle("${track.trackName}")
//            .setStyle(NotificationCompat.BigTextStyle()
//                .bigText("${track.artistName}"))
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(createPendingIntentForNotification())
//            .addAction(R.drawable.baseline_play_arrow_24, status,
//            createSnoozPendIntent())
//            .build()



}

    private fun createNotificationWithProgressBar(track: PlayerTrack): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle("${track.artistName}")
            setContentText("${track.trackName}")
            setSmallIcon(R.drawable.ic_agreement_icon)
            setPriority(NotificationCompat.PRIORITY_LOW)
            setSilent(true)
        }

        val PROGRESS_MAX = 100
        val PROGRESS_CURRENT = 0
        NotificationManagerCompat.from(this).apply {
            // Issue the initial notification with zero progress
            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            coroutineScope.launch {
                var progress = 0
                repeat(10) {
                    progress += 10
                    builder.setProgress(100, progress, false)
                    notify(NOTIFICATION_ID, builder.build())
                    delay(1000)
                }
                builder.setContentText("complete")
                    .setProgress(100, 0, false)
                notify(NOTIFICATION_ID, builder.build())

            }
        }
        return builder
    }

    private fun notificationWithMediaControl(status: String, track: PlayerTrack): NotificationCompat.Builder {
        val stopPlayIntent = Intent().apply {
            action = ACTION
        }

        val pausePendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext, 0, stopPlayIntent, 0)
        val prevPendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext, 0, stopPlayIntent, 0)
        val nextPendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext, 0, stopPlayIntent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            // Show controls on lock screen even when user hides sensitive content.
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_share_icon)
            // Add media control buttons that invoke intents in your media service
            .addAction(R.drawable.ic_arrow_icon, "Previous", prevPendingIntent) // #0
            .addAction(R.drawable.baseline_pause_24, status, pausePendingIntent) // #1
            .addAction(R.drawable.baseline_favorite_full, "Next", nextPendingIntent) // #2
            // Apply the media style template
//            .setStyle(
//                MediaNotificationCompat.MediaStyle()
//                    .setShowActionsInCompactView(1 /* #1: pause button \*/)
//                    .setMediaSession(mediaSession.getSessionToken())
//            )
            .setSilent(true)
            .setContentTitle("${track.trackName}")
            .setContentText("${track.artistName}")
//            .setLargeIcon(albumArtBitmap)


        val PROGRESS_MAX = 100
        var PROGRESS_CURRENT = 0
        NotificationManagerCompat.from(this).apply {
            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
            }
            Log.e(TAG, "notificationWithMediaControl: $status", )
            if (status == PAUSE) {
                progressJob =   coroutineScope.launch {

                    repeat(10) {
                        currentTimeProgress += 10
                        builder.setProgress(PROGRESS_MAX, currentTimeProgress, false)
                        notify(NOTIFICATION_ID, builder.build())
                        delay(1000L)
                    }
                    currentTimeProgress = 0
                    builder.setContentText("complete")
                        .setProgress(PROGRESS_MAX, currentTimeProgress, false)
                    notify(NOTIFICATION_ID, builder.build())
                }
            }else{
                progressJob?.cancel()
                builder.setProgress(PROGRESS_MAX, currentTimeProgress, false)
//                coroutineScope.cancel()
            }
        }
return builder
    }

    private fun createSnoozPendIntent(): PendingIntent {
        val snoozeIntent = Intent().apply {
            action = ACTION
        }
        return PendingIntent.getBroadcast(applicationContext, 0, snoozeIntent, 0)
    }

    private fun createPendingIntentForNotification(): PendingIntent {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
       return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    companion object{
        private const val TAG = "PlayerService"
        const val TRACK = "track"
        const val ACTION = "rururu"
        private const val CHANNEL_NAME = "channel"
        private const val CHANNEL_ID = "1"
        private const val NOTIFICATION_ID = 1
        private const val PLAY ="PLAY"
        private const val PAUSE ="PAUSE"

        fun newIntent(context: Context, track: PlayerTrack): Intent{
            return Intent(context, PlayerService::class.java).apply {
                putExtra(TRACK, track)
            }
        }
    }


}
