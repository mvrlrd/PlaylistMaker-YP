package ru.mvrlrd.playlistmaker.player.ui


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer.PlayerState.*
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.util.formatTime
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack


class PlayerViewModel(val playerTrack: PlayerTrack, private val playerInteractor: PlayerInteractor) : ViewModel() {
    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> = _screenState

    val playerState = playerInteractor.getLivePlayerState()

    private var timerJob: Job? = null

    val playlists: LiveData<List<PlaylistForAdapter>> =
        playerInteractor.getAllPlaylists().asLiveData()

    init {
            playerInteractor.preparePlayer(playerTrack)
    }


    fun render() {
        when (playerState.value) {
            DEFAULT -> {
                _screenState.value = PlayerScreenState.BeginningState(playerTrack)
            }
            PREPARED -> {
                _screenState.value = PlayerScreenState.Preparing
            }
            PLAYING -> {

              timerJob =  viewModelScope.launch {
                    while (playerState.value == PLAYING) {
                        delay(300)
                         playerInteractor.getCurrentTime().collect(){

                             _screenState.value = PlayerScreenState.Playing(getCurrentPosition(it))
                         }
                    }
                }
                _screenState.value =  PlayerScreenState.PlayButtonHandling(playerState.value!!)
            }
            PAUSED -> {
                timerJob?.cancel()
                _screenState.value = PlayerScreenState.PlayButtonHandling(playerState.value!!)
            }
            COMPLETED -> {
                timerJob?.cancel()
                _screenState.value = PlayerScreenState.PlayCompleting
            }
            else -> {}
        }
    }

    fun onResume(){
        when(playerState.value){

            DEFAULT -> {
//                _screenState.value = PlayerScreenState.BeginningState(playerTrack)
//                playerInteractor.preparePlayer(playerTrack)
            }
            PAUSED,PLAYING, ->{
                _screenState.value = PlayerScreenState.BeginningState(playerTrack)
                _screenState.value = PlayerScreenState.Preparing
            }
            COMPLETED,PREPARED, ->{
                _screenState.value = PlayerScreenState.BeginningState(playerTrack)
                _screenState.value = PlayerScreenState.Preparing
                _screenState.value = PlayerScreenState.PlayCompleting
            }
            else ->{}
        }
    }

    fun onStop(){
        playerInteractor.pause()
    }

    fun renderTime(time: Int){
        _screenState.value = PlayerScreenState.Playing(getCurrentPosition(time))
    }

    fun playbackControl() {
        when (playerState.value) {
            PLAYING -> {
                pause()
            }
            PREPARED, PAUSED, COMPLETED -> {
                start()
            }
            else->{}
        }
    }
    private fun start() {
        playerInteractor.start()
    }

    fun pause() {
        playerInteractor.pause()
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



    fun onDestroy(){
        timerJob?.cancel()
        playerInteractor.onDestroy()
    }

    private fun getCurrentPosition(_time: Int):String{
        return formatTime(_time)
    }


}


