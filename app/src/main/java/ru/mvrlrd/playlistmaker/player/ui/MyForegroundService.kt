package ru.mvrlrd.playlistmaker.player.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer
import ru.mvrlrd.playlistmaker.player.data.PlayerClient
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor


class MyForegroundService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
//https://medium.com/@androidtechsolution/a-foreground-service-to-play-music-fcb1c5d1cf59
    private  val mediaPlayer: PlayerInteractor by inject()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        log("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        val action = intent?.getStringExtra(EXTRA)?:""
        log("onStartCommand $action")
        coroutineScope.launch {
            if (action == STARTFOREGROUND_ACTION){
                mediaPlayer.handleStartAndPause()
            }
            if (action == STOPFOREGROUND_ACTION){
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun log(message: String){
        Log.d(TAG, message)
    }

    private fun createNotificationChannel(){
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    private fun createNotification(): Notification {
        val stopIntent = Intent(this, MyForegroundService::class.java)
        stopIntent.action = STOPFOREGROUND_ACTION
        val pendingStopIntent = PendingIntent.getService(this, 0, stopIntent, FLAG_CANCEL_CURRENT)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("PlaylistMaker")
            .setContentText("playing music")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .addAction(R.drawable.baseline_pause_24, "Pause", pendingStopIntent)
            .build()
    }


    companion object{
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "foregroundsss"
        private const val TAG = "SERVICE_TAG"
        private const val EXTRA = "track"
        const val STARTFOREGROUND_ACTION = "start"
        const val STOPFOREGROUND_ACTION = "stop"

        fun newIntent(context: Context, action: String): Intent {
            val intent = Intent(context, MyForegroundService::class.java).apply {
                putExtra(EXTRA, action)
            }
            return intent
        }
    }

}