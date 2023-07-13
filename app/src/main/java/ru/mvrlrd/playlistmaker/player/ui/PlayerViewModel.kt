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
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.player.ui.PlayerState.*

class PlayerViewModel(val playerTrack: PlayerTrack, private val playerInteractor: PlayerInteractor) : ViewModel() {
    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> = _screenState
    private var playerState: PlayerState = DEFAULT
    private var timerJob: Job? = null

    init {
        _screenState.value = PlayerScreenState.BeginningState(playerTrack)
        preparePlayer(playerTrack)
        setOnCompletionListener()
    }

    private fun preparePlayer(playerTrack: PlayerTrack){
        playerInteractor.preparePlayer(playerTrack) {
            playerState = PREPARED
            _screenState.value = PlayerScreenState.Preparing
        }
    }

    fun handleLikeButton(){
        playerTrack.isFavorite = !playerTrack.isFavorite
        _screenState.postValue(PlayerScreenState.LikeButtonHandler(playerTrack))
        viewModelScope.launch {
            if (!playerTrack.isFavorite){
                playerInteractor.removeTrackFromFavorite(playerTrack.trackId)
            }else{
                playerInteractor.addTrackToFavorite(playerTrack)
            }
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

    fun playbackControl() {
        when (playerState) {
            PLAYING -> {
                pause()
            }
            PREPARED, PAUSED -> {
                start()
            }
            DEFAULT -> {}
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

    private fun getCurrentPosition():String{
        return formatTime(playerInteractor.getCurrentTime())
    }

    private fun setOnCompletionListener() {
        playerInteractor.setOnCompletionListener {
            playerState = PREPARED
            timerJob?.cancel()
            _screenState.value = PlayerScreenState.PlayCompleting
        }
    }

    fun onDestroy(){
        timerJob?.cancel()
        playerState = DEFAULT
        playerInteractor.onDestroy()
    }
}

enum class PlayerState {
     DEFAULT,
     PREPARED,
     PLAYING,
     PAUSED
}
