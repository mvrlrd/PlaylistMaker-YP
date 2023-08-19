package ru.mvrlrd.playlistmaker.player.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.main.MainActivity
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import java.util.*
import kotlin.properties.Delegates


class PlayerService : Service() {
    private val binder = LocalBinder()
    private val interactor: PlayerInteractor by inject()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null

   private val _curr = MutableStateFlow(-1L)
    val curr get() = _curr.asStateFlow()

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val track = intent?.getSerializableExtra(TRACK) as PlayerTrack
        if (curr.value != track.trackId) {
            interactor.onDestroy()
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
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun createNotification() : Notification {
        createNotificationChannel()

        var pendingIntentFlag by Delegates.notNull<Int>()
        pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }


        createPendingIntentForNotification()


        createSnoozPendIntent()


        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_agreement_icon)
            .setContentTitle("My notification")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(createPendingIntentForNotification())
            .addAction(R.drawable.baseline_play_arrow_24, "soso",
            createSnoozPendIntent())
            .build()



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

        fun newIntent(context: Context, track: PlayerTrack): Intent{
            return Intent(context, PlayerService::class.java).apply {
                putExtra(TRACK, track)
            }
        }
    }


}