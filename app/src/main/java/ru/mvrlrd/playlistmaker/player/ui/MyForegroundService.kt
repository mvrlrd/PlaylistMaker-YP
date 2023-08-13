package ru.mvrlrd.playlistmaker.player.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.main.MainActivity
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import kotlin.properties.Delegates


class MyForegroundService(
   private val context: Context,
    private val workerParameters: WorkerParameters
) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {


            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                }
                notify(0, createNotification())

        }

        log("doWork")
        val page = workerParameters.inputData.getInt(PAGE, 0)
        for (i in 0 until 5) {
            Thread.sleep(1000)
            log("timer $i page = $page")
        }
        return Result.success()
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    companion object {
        private const val PAGE = "page"
        private const val TAG = "MyWorker"
        const val WORK_NAME = "name"

        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyForegroundService>()
                .setInputData(workDataOf(PAGE to page))
                .setConstraints(makeConstraints())
                .build()
        }

        private fun makeConstraints() = Constraints.Builder()
            .setRequiresCharging(false)
            .build()
    }



    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                12.toString(),
                "DemoWorker",
                NotificationManager.IMPORTANCE_DEFAULT,
            )

            val notificationManager: NotificationManager? =
                ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                )

            notificationManager?.createNotificationChannel(
                notificationChannel
            )
        }
    }
    private fun createNotification() : Notification {
        createNotificationChannel()

        val mainActivityIntent = Intent(
            context,
            MainActivity::class.java)

        var pendingIntentFlag by Delegates.notNull<Int>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntentFlag = PendingIntent.FLAG_IMMUTABLE
        } else {
            pendingIntentFlag = PendingIntent.FLAG_UPDATE_CURRENT
        }

        val mainActivityPendingIntent = PendingIntent.getActivity(
            context,
            0,
            mainActivityIntent,
            pendingIntentFlag)


        return NotificationCompat.Builder(
            context,
            "12"
        )
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("Work Request Done!")
            .setContentIntent(mainActivityPendingIntent)
            .setAutoCancel(true)
            .build()
    }

}



//    Service() {
//    private val coroutineScope = CoroutineScope(Dispatchers.Main)
////https://medium.com/@androidtechsolution/a-foreground-service-to-play-music-fcb1c5d1cf59
//    private  val interactor: PlayerInteractor by inject()
//    private val scope = CoroutineScope(Dispatchers.IO)
//
//    override fun onCreate() {
//        super.onCreate()
//        createNotificationChannel()
//        startForeground(NOTIFICATION_ID, createNotification())
//        log("onCreate")
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        log("onStartCommand")
////        val action = intent?.getStringExtra(EXTRA)?:""
//        val track = intent?.getSerializableExtra(TRACK) as PlayerTrack
//        log("onStartCommand ${interactor}")
//
//       val job = scope.launch {
////            if (action == STARTFOREGROUND_ACTION){
//                interactor.onDestroy()
//                interactor.preparePlayer(track)
//                interactor.handleStartAndPause()
//
//            }
//
////            if (action == STOPFOREGROUND_ACTION){
////                interactor.onDestroy()
////                stopSelf()
////            }
////        }
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        coroutineScope.cancel()
//        log("onDestroy")
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {

//    }
//
//    private fun log(message: String){
//        Log.d(TAG, message)
//    }
//
//    private fun createNotificationChannel(){
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationChannel = NotificationChannel(
//                CHANNEL_ID,
//                CHANNEL_NAME,
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//    }
//    private fun createNotification(): Notification {
//        val stopIntent = Intent(this, MyForegroundService::class.java)
//        stopIntent.action = STOPFOREGROUND_ACTION
//        val pendingStopIntent = PendingIntent.getService(this, 0, stopIntent, FLAG_CANCEL_CURRENT)
//
//        return NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("PlaylistMaker")
//            .setContentText("playing music")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .addAction(R.drawable.baseline_pause_24, "Pause", pendingStopIntent)
//            .build()
//    }
//
//
//    companion object{
//        private const val NOTIFICATION_ID = 1
//        private const val CHANNEL_ID = "channel_id"
//        private const val CHANNEL_NAME = "foregroundsss"
//        private const val TAG = "SERVICE_TAG"
//        private const val EXTRA = "track"
//        const val STARTFOREGROUND_ACTION = "start"
//        const val STOPFOREGROUND_ACTION = "stop"
//        const val TRACK = "track"
//
//        fun newIntent(context: Context, action: String, track: PlayerTrack): Intent {
//            val intent = Intent(context, MyForegroundService::class.java).apply {
//                putExtra(EXTRA, action)
//                putExtra(TRACK, track)
//            }
//            return intent
//        }
//    }
//
//}