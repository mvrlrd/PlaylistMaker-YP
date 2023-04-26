package ru.mvrlrd.playlistmaker.player.ui


import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mvrlrd.playlistmaker.Creator
import ru.mvrlrd.playlistmaker.formatTime
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer
import ru.mvrlrd.playlistmaker.player.ui.PlayerState.*

class PlayerViewModel(trackForPlayer: TrackForPlayer) : ViewModel() {
    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> = _screenState
    private val playerInteractor = Creator.providePlayerInteractor(trackForPlayer)
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
        _screenState.value = PlayerScreenState.Preview(trackForPlayer)
        preparePlayer()
        setOnCompletionListener()
    }

    private fun start() {
        playerInteractor.start()
        playerState = STATE_PLAYING
        handler.postDelayed(timerGo, PlayerActivity.REFRESH_TIMER_DELAY_MILLIS)
        _screenState.value = PlayerScreenState.StartStop(playerState)
    }
    private fun pause() {
        playerInteractor.pause()
        playerState = STATE_PAUSED
        handler.removeCallbacks(timerGo)
        _screenState.value = PlayerScreenState.StartStop(playerState)
    }

    fun onDestroy(){
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        playerInteractor.onDestroy()
    }

    private fun updateTimer(time: String) {
        _screenState.postValue(PlayerScreenState.Timer(time))
    }

    private fun getCurrentPosition():String{
        return formatTime(playerInteractor.getCurrentTime())
    }

    private fun setOnCompletionListener() {
        playerInteractor.setOnCompletionListener {
            playerState = STATE_PREPARED
            handler.removeCallbacks(timerGo)
            _screenState.value = PlayerScreenState.CompletePlaying()
        }
    }

    private fun preparePlayer(){
        playerInteractor.preparePlayer {
            _screenState.value = PlayerScreenState.Prepare()
            playerState = STATE_PREPARED
        }
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
        private const val REFRESH_TIMER_DELAY_MILLIS = 1000L
        private val SEARCH_REQUEST_TOKEN = Any()
        fun getViewModelFactory(trackForPlayer: TrackForPlayer): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        trackForPlayer
                    ) as T
                }
            }
    }
}
enum class PlayerState{
    STATE_DEFAULT,
    STATE_PREPARED,
    STATE_PLAYING,
    STATE_PAUSED
}