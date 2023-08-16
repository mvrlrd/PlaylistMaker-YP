package ru.mvrlrd.playlistmaker.player.ui

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import java.util.*



class PlayerService : Service() {
    // Binder given to clients.
    private val binder = LocalBinder()
    private val interactor: PlayerInteractor by inject()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null

   private val _curr = MutableStateFlow(-1L)
    val curr get() = _curr.asStateFlow()

    // Random number generator.
    private val mGenerator = Random()

    /** Method for clients.  */
    val randomNumber: Int
        get() = mGenerator.nextInt(100)

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


    companion object{
        private const val TAG = "PlayerService"
        const val TRACK_ID = "trackId"
        const val TRACK = "track"

         fun newIntent(context: Context, track: PlayerTrack): Intent{
            return Intent(context, PlayerService::class.java).apply {
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