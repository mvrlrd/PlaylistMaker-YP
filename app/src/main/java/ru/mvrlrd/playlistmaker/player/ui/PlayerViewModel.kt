package ru.mvrlrd.playlistmaker.player.ui


import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.FileOperatingViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.GetInternalFileUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer.PlayerState.*
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.player.util.formatTime
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter


class PlayerViewModel(
    val track: PlayerTrack,
    private val interactor: PlayerInteractor,
    fileHandler: GetInternalFileUseCase
) : FileOperatingViewModel(fileHandler) {

    private val _screenState = MutableSharedFlow<PlayerScreenState>(5)
    val screenState = _screenState.asSharedFlow()

    private var timerJob: Job? = null

    init {
        interactor.preparePlayer(track)
        loadLike()
        observePlayerState()
        loadPlaylists()
    }


    private fun loadPlaylists(){
        interactor.getAllPlaylistsWithQuantities()
            .filter { it.isNotEmpty() }
            .onEach { println(it) }
            .map { PlayerScreenState.UpdatePlaylistList(it, track) }
            .onEach {
               _screenState.emit(it)
            }
            .launchIn(viewModelScope)
    }


    private fun observePlayerState() {
        interactor.getLivePlayerState()
            .onEach {
                render(it)
            }
            .launchIn(viewModelScope)
    }
    private fun loadLike(){
        interactor.getFavIds()
            .onEach {
                track.isFavorite = it.contains(track.trackId)
                _screenState.emit(PlayerScreenState.HandleLikeButton(track))
            }
            .launchIn(viewModelScope)
    }

    fun addOrRemoveFromFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            if (track.isFavorite) {
                interactor.removeTrackFromFavorite(track.trackId)
            } else {
                interactor.addTrackToFavorite(track)
            }
        }
    }

    fun render(playerState: MyMediaPlayer.PlayerState) {
        timerJob?.cancel()
        Log.d(TAG, "$PLAYER_STATE_MESSAGE ${playerState.name}")
        when (playerState) {
            ERROR -> {
//                _screenState.value = PlayerScreenState.PlayerError
            }
            DEFAULT -> {
                _screenState.tryEmit(PlayerScreenState.BeginningScreenState(track))
            }
            PREPARED -> {
                _screenState.tryEmit(PlayerScreenState.EnablePlayButton(track))
            }
            PLAYING -> {
                _screenState.tryEmit(PlayerScreenState.StartPlaying(track))
                timerJob = viewModelScope.launch {
                    while (true) {
                        delay(TIMER_REFRESH_DELAY_TIME)
                        interactor.getCurrentTime().collect() {
                            _screenState.tryEmit(PlayerScreenState.RenderTrackTimer(formatTime(it), track))
                        }
                    }
                }
            }
            PAUSED -> {
                _screenState.tryEmit(PlayerScreenState.StopPlaying(track))
            }
            COMPLETED -> {
//                _screenState.value = PlayerScreenState.PlayCompleting
            }
            STOPPED -> {
                timerJob = viewModelScope.launch {
                    interactor.getCurrentTime().collect() {
//                        _screenState.tryEmit(PlayerScreenState.RenderTrackTimer(formatTime(it), track))
                    }
                }
                _screenState.tryEmit(PlayerScreenState.LoadAfterBackgrounded(track))
            }
        }
    }


    fun playbackControl() {
        interactor.handleStartAndPause()
    }


    fun onStop() {
        interactor.pause()
    }

    fun putItOnBackground(){
        interactor.stopIt()
    }


    fun onDestroy() {
        timerJob?.cancel()
        interactor.onDestroy()
    }

    fun addTrackToPlaylist(_track: TrackForAdapter, playlist: PlaylistForAdapter) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.addTrackToPlaylist(trackId = _track, playlist = playlist)
                .collect() {
                    _screenState.emit(PlayerScreenState.AddTrackToPlaylist(track = track, result = it))
                }
        }
    }

    companion object{
        private const val TIMER_REFRESH_DELAY_TIME = 300L
        private const val TAG = "PlayerViewModel"
        private const val PLAYER_STATE_MESSAGE = "player state:"
    }
}


