package ru.mvrlrd.playlistmaker.player.ui

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import java.util.*



class PlayerService : Service() {
    // Binder given to clients.
    private val binder = LocalBinder()
    private val interactor: PlayerInteractor by inject()
    private var currentId = -1L
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val coroutineScope2 = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null


    // Random number generator.
    private val mGenerator = Random()

    /** Method for clients.  */
    val randomNumber: Int
        get() = mGenerator.nextInt(100)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand: ", )
        val trackId = intent?.getLongExtra(TRACK_ID, -1)?:-1
        val track = intent?.getSerializableExtra(TRACK) as PlayerTrack
        Log.e(TAG, "onStartCommand: ${track}", )
        if (currentId == -1L){
            handlePlaying()
            currentId = trackId
        }else if (currentId != trackId){
            currentId = trackId
            interactor.onDestroy()
           job =  coroutineScope.launch {

                 interactor.prp(track)

            }
        }else{
            handlePlaying()
        }

        return super.onStartCommand(intent, flags, startId)

    }

    fun handlePlaying(){
        interactor.handleStartAndPause()
    }


    companion object{
        private const val TAG = "PlayerService"
        const val TRACK_ID = "trackId"
        const val TRACK = "track"

         fun newIntent(context: Context, id: Long, track: PlayerTrack): Intent{
            return Intent(context, PlayerService::class.java).apply {
                putExtra(TRACK_ID, id)
                putExtra(TRACK, track)
            }
        }
    }


    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */


    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods.
        fun getService(): PlayerService = this@PlayerService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }
}