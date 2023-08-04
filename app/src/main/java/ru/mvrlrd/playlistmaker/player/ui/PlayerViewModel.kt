package ru.mvrlrd.playlistmaker.player.ui


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.FileOperatingViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.GetInternalFileUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer.PlayerState.*
import ru.mvrlrd.playlistmaker.player.domain.AddingTrackToPlaylistResult
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.util.formatTime
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter


class PlayerViewModel(
    val track: PlayerTrack,
    private val interactor: PlayerInteractor,
    fileHandler: GetInternalFileUseCase
) : FileOperatingViewModel(fileHandler) {

    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> = _screenState

    val playerState = interactor.getLivePlayerState()

    val playlists = interactor.getAllPlaylistsWithQuantities()

    private val _isTrackInPlaylist = MutableLiveData<AddingTrackToPlaylistResult>()
    val isTrackInPlaylist: LiveData<AddingTrackToPlaylistResult> = _isTrackInPlaylist


    private var timerJob: Job? = null

    init {
        loadLike()
        interactor.preparePlayer(track)
    }

    private fun loadLike(){
        interactor.getFavIds()
            .onStart {
                _screenState.postValue(PlayerScreenState.BlockLikeButton)
            }
            .onEach {
                track.isFavorite = it.contains(track.trackId)
                _screenState.value = PlayerScreenState.HandleLikeButton(track.isFavorite)
            }
            .launchIn(viewModelScope)
    }

    fun handleLikeButton() {
        viewModelScope.launch {
            if (track.isFavorite) {
                interactor.removeTrackFromFavorite(track.trackId)
            } else {
                interactor.addTrackToFavorite(track)
            }
        }
    }

    fun addTrackToPlaylist(track: TrackForAdapter, playlist: PlaylistForAdapter) {

        viewModelScope.launch {
            interactor.addTrackToPlaylist(trackId = track, playlist = playlist)
                .collect() {
                    _isTrackInPlaylist.postValue(it)
                }
        }
    }



    fun render(plState: MyMediaPlayer.PlayerState) {
        timerJob?.cancel()
        Log.d(TAG, "$PLAYER_STATE_MESSAGE ${plState.name}")
        when (plState) {
            ERROR -> {
                _screenState.value = PlayerScreenState.PlayerError(track)
            }
            DEFAULT -> {
                _screenState.value = PlayerScreenState.BeginningState(track)
            }
            PREPARED -> {
                _screenState.value = PlayerScreenState.Preparing
            }
            PLAYING -> {
                timerJob = viewModelScope.launch {
                    while (true) {
                        delay(TIMER_REFRESH_DELAY_TIME)
                        interactor.getCurrentTime().collect() {
                            renderTime(it)
                        }
                    }
                }
                _screenState.value = PlayerScreenState.PlayButtonHandlingSTOP
                _screenState.value = PlayerScreenState.PlayButtonHandling(plState)
            }
            PAUSED -> {
                _screenState.value = PlayerScreenState.PlayButtonHandlingSTART
            }
            COMPLETED -> {
                _screenState.value = PlayerScreenState.PlayCompleting
            }
            STOPPED -> {
                timerJob = viewModelScope.launch {
                    interactor.getCurrentTime().collect() {
                        renderTime(it)
                    }
                }
                _screenState.value = PlayerScreenState.BeginningState(track)
                _screenState.value = PlayerScreenState.Preparing
            }
        }
    }


    private fun renderTime(time: Int) {
        _screenState.value = PlayerScreenState.Playing(formatTime(time))
    }

    fun playbackControl() {
        interactor.handleStartAndPause()
    }



    fun onResume(){
        interactor.stopIt()
    }

    fun onStop() {
        interactor.pause()
    }
    fun onDestroy() {
        timerJob?.cancel()
        interactor.onDestroy()
    }

    companion object{
        private const val TIMER_REFRESH_DELAY_TIME = 300L
        private const val TAG = "PlayerViewModel"
        private const val PLAYER_STATE_MESSAGE = "player state:"
    }
}


