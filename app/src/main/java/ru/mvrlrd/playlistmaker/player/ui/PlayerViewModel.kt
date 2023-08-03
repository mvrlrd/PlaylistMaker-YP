package ru.mvrlrd.playlistmaker.player.ui


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.FileOperatingViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.GetInternalFileUseCase
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer.PlayerState.*
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.util.formatTime
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter


class PlayerViewModel(
    val playerTrack: PlayerTrack,
    private val playerInteractor: PlayerInteractor,
    private val fileUseCase: GetInternalFileUseCase
) : FileOperatingViewModel(fileUseCase) {
    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> = _screenState

    val favoriteIds = playerInteractor.getFavIds()

    val playerState = playerInteractor.getLivePlayerState()

    private var timerJob: Job? = null

    val playlists = playerInteractor.getAllPlaylistsWithQuantities()

    private val _isTrackInPlaylist = MutableLiveData<Pair<String, Boolean>>()
    val isTrackInPlaylist: LiveData<Pair<String, Boolean>> = _isTrackInPlaylist

    fun handleLike(favIds: List<Long>, trackId: Long) {
        playerTrack.isFavorite = favIds.contains(trackId)
        _screenState.value = PlayerScreenState.LikeHandle(playerTrack.isFavorite)
    }
    init {
        playerInteractor.preparePlayer(playerTrack)
    }

    fun addTrackToPlaylist(trackId: TrackForAdapter, playlistId: Long) {
        viewModelScope.launch {
            playerInteractor.addTrackToPlaylist(trackId = trackId, playlistId = playlistId)
                .collect() {
                    _isTrackInPlaylist.value = it
                }
        }
    }

    fun render(plState: MyMediaPlayer.PlayerState) {
        timerJob?.cancel()
        Log.d(TAG, "$PLAYER_STATE_MESSAGE ${plState.name}")
        when (plState) {
            ERROR -> {
                _screenState.value = PlayerScreenState.PlayerError(playerTrack)
            }
            DEFAULT -> {
                _screenState.value = PlayerScreenState.BeginningState(playerTrack)
            }
            PREPARED -> {
                _screenState.value = PlayerScreenState.Preparing
            }
            PLAYING -> {
                timerJob = viewModelScope.launch {
                    while (true) {
                        delay(TIMER_REFRESH_DELAY_TIME)
                        playerInteractor.getCurrentTime().collect() {
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
                    playerInteractor.getCurrentTime().collect() {
                        renderTime(it)
                    }
                }
                _screenState.value = PlayerScreenState.BeginningState(playerTrack)
                _screenState.value = PlayerScreenState.Preparing
            }
        }
    }


    private fun renderTime(time: Int) {
        _screenState.value = PlayerScreenState.Playing(formatTime(time))
    }

    fun playbackControl() {
        playerInteractor.handleStartAndPause()
    }

    fun handleLikeButton() {
        viewModelScope.launch {
            if (playerTrack.isFavorite) {
                playerInteractor.removeTrackFromFavorite(playerTrack.trackId)
            } else {
                playerInteractor.addTrackToFavorite(playerTrack)
            }
        }
    }

    fun onResume(){
        playerInteractor.stopIt()
    }

    fun onStop() {
        playerInteractor.pause()
    }
    fun onDestroy() {
        timerJob?.cancel()
        playerInteractor.onDestroy()
    }

    companion object{
        private const val TIMER_REFRESH_DELAY_TIME = 300L
        private const val TAG = "PlayerViewModel"
        private const val PLAYER_STATE_MESSAGE = "player state:"
    }
}


