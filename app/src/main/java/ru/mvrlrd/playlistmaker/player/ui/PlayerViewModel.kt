package ru.mvrlrd.playlistmaker.player.ui


import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.util.formatTime
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer
import ru.mvrlrd.playlistmaker.player.ui.PlayerState.*

class PlayerViewModel(private val trackForPlayer: TrackForPlayer, private val playerInteractor: PlayerInteractor) : ViewModel() {
    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> = _screenState
    private var playerState: PlayerState = STATE_DEFAULT
    private val handler: Handler = Handler(Looper.getMainLooper())

    private val timerGo =
        object : Runnable {
            override fun run() {
                updateTimer(getCurrentPosition())
                handler.postDelayed(
                    this,
                    REFRESH_TIMER_DELAY_MILLIS,
                )
            }
        }
    init {
        _screenState.value = PlayerScreenState.BeginningState(trackForPlayer)
        preparePlayer()
        setOnCompletionListener()
    }
    private fun preparePlayer(){
        playerInteractor.preparePlayer(trackForPlayer) {
            playerState = STATE_PREPARED
            _screenState.value = PlayerScreenState.Preparing()
        }
    }
    private fun setOnCompletionListener() {
        playerInteractor.setOnCompletionListener {
            playerState = STATE_PREPARED
            handler.removeCallbacks(timerGo)
            _screenState.value = PlayerScreenState.PlayCompleting()
        }
    }
    private fun start() {
        playerInteractor.start()
        playerState = STATE_PLAYING
        handler.postDelayed(timerGo, REFRESH_TIMER_DELAY_MILLIS)
        _screenState.value = PlayerScreenState.PlayButtonHandling(playerState)
    }
    fun pause() {
        playerInteractor.pause()
        playerState = STATE_PAUSED
        handler.removeCallbacks(timerGo)
        _screenState.value = PlayerScreenState.PlayButtonHandling(playerState)
    }
    private fun updateTimer(time: String) {
        _screenState.postValue(PlayerScreenState.TimerUpdating(time))
    }

    private fun getCurrentPosition():String{
        return formatTime(playerInteractor.getCurrentTime())
    }

    fun onDestroy(){
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        playerInteractor.onDestroy()
    }

    fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pause()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                start()
            }
            STATE_DEFAULT -> {

            }
        }
    }

    companion object {
        private const val REFRESH_TIMER_DELAY_MILLIS = 300L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}
enum class PlayerState{
    STATE_DEFAULT,
    STATE_PREPARED,
    STATE_PLAYING,
    STATE_PAUSED
}