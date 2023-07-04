package ru.mvrlrd.playlistmaker.player.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.util.formatTime
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer
import ru.mvrlrd.playlistmaker.player.ui.PlayerState.*

class PlayerViewModel(trackForPlayer: TrackForPlayer, private val playerInteractor: PlayerInteractor) : ViewModel() {
    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> = _screenState
    private var playerState: PlayerState = DEFAULT
    private var timerJob: Job? = null


    init {
        _screenState.value = PlayerScreenState.BeginningState(trackForPlayer)
        preparePlayer(trackForPlayer)
        setOnCompletionListener()
    }

    fun addToFavorite(trackForPlayer: TrackForPlayer){
        viewModelScope.launch {
            playerInteractor.addTrackToFavorite(trackForPlayer)
        }
    }

    private fun startTimer(){
        timerJob = viewModelScope.launch {
            while (playerState == PLAYING) {
                delay(300L)
                _screenState.postValue(PlayerScreenState.Playing(getCurrentPosition()))
            }
        }
    }
    private fun preparePlayer(trackForPlayer: TrackForPlayer){
        playerInteractor.preparePlayer(trackForPlayer) {
            playerState = PREPARED
            _screenState.value = PlayerScreenState.Preparing()
        }
    }
    private fun setOnCompletionListener() {
        playerInteractor.setOnCompletionListener {
            playerState = PREPARED
            timerJob?.cancel()
            _screenState.value = PlayerScreenState.PlayCompleting()
        }
    }
    private fun start() {
        playerInteractor.start()
        playerState = PLAYING
        startTimer()
        _screenState.value =  PlayerScreenState.PlayButtonHandling(playerState)
    }
    fun pause() {
        playerInteractor.pause()
        timerJob?.cancel()
        playerState = PAUSED
        _screenState.value =  PlayerScreenState.PlayButtonHandling(playerState)
    }

    private fun getCurrentPosition():String{
        return formatTime(playerInteractor.getCurrentTime())
    }

    fun onDestroy(){
        playerState = DEFAULT
        playerInteractor.onDestroy()
    }

    fun playbackControl() {
        when (playerState) {
             PLAYING -> {
                pause()
            }
             PREPARED,  PAUSED -> {
                start()
            }
            DEFAULT -> {

            }
        }
    }
}

enum class PlayerState {

     DEFAULT,

     PREPARED,

     PLAYING,

     PAUSED

}
