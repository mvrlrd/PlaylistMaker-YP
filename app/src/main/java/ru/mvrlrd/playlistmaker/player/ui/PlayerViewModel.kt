package ru.mvrlrd.playlistmaker.player.ui


import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
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

    private val _screenState = MutableSharedFlow<PlayerScreenState>(10)


    private val playlistsFlow = interactor.getAllPlaylistsWithQuantities()
        .map { PlayerScreenState.UpdatePlaylistList(it)  }
    private val likesFlow = interactor.getFavIds()
        .map {
            track.isFavorite = it.contains(track.trackId)
            PlayerScreenState.HandleLikeButton(track.isFavorite)
        }

    private val _mergedStates = _screenState.mergeWith(playlistsFlow, likesFlow)
    val mergedStates = _mergedStates

    private var timerJob: Job? = null

    init {
        interactor.preparePlayer(track)
        observePlayerState()
    }

    private fun observePlayerState() {
        interactor.getLivePlayerState()
            .onEach {
                render(it)
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
        Log.e(TAG, "$PLAYER_STATE_MESSAGE ${playerState.name}")
        when (playerState) {
            ERROR -> {
//                _screenState.value = PlayerScreenState.PlayerError
            }
            DEFAULT -> {
                _screenState.tryEmit(PlayerScreenState.DisableFabs)
                _screenState.tryEmit(PlayerScreenState.LoadTrackInfo(track))

            }
            PREPARED -> {
                _screenState.tryEmit(PlayerScreenState.EnablePlayButton)
            }
            PLAYING -> {
                _screenState.tryEmit(PlayerScreenState.StartPlaying)
                timerJob = viewModelScope.launch {
                    while (true) {
                        delay(TIMER_REFRESH_DELAY_TIME)
                        interactor.getCurrentTime().collect() {
                            _screenState.tryEmit(PlayerScreenState.RenderTrackTimer(formatTime(it)))
                        }
                    }
                }
            }
            PAUSED -> {
                _screenState.tryEmit(PlayerScreenState.StopPlaying)
            }
            COMPLETED -> {
                _screenState.tryEmit(PlayerScreenState.PlayCompleting)
            }
            STOPPED -> {
                timerJob = viewModelScope.launch {
                    interactor.getCurrentTime().collect() {
                        _screenState.tryEmit(PlayerScreenState.RenderTrackTimer(formatTime(it)))
                    }
                }
                _screenState.tryEmit(PlayerScreenState.EnablePlayButton)
                _screenState.tryEmit(PlayerScreenState.LoadTrackInfo(track))
                _screenState.tryEmit(PlayerScreenState.HandleLikeButton(track.isFavorite))

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

    private fun Flow<PlayerScreenState>.mergeWith(another: Flow<PlayerScreenState>, another2 : Flow<PlayerScreenState>): Flow<PlayerScreenState>{
        return merge(this, another, another2)
    }

    fun addTrackToPlaylist(_track: TrackForAdapter, playlist: PlaylistForAdapter) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.addTrackToPlaylist(trackId = _track, playlist = playlist)

                .collect() {
                    _screenState.emit(PlayerScreenState.AddTrackToPlaylist( result = it))
                }
        }
    }

    companion object{
        private const val TIMER_REFRESH_DELAY_TIME = 300L
        private const val TAG = "PlayerViewModel"
        private const val PLAYER_STATE_MESSAGE = "player state:"
    }
}


