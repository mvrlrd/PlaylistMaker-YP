package ru.mvrlrd.playlistmaker.player.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer.PlayerState.*
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.util.formatTime
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter


class PlayerViewModel(
    val playerTrack: PlayerTrack,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {
    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> = _screenState

    val favoriteIds = playerInteractor.getFavIds()

    val playerState = playerInteractor.getLivePlayerState()

    private var timerJob: Job? = null

    val playlists: LiveData<List<PlaylistForAdapter>> =
        playerInteractor.getAllPlaylistsWithQuantities().asLiveData()

    private val _isTrackInPlaylist = MutableLiveData<Pair<String, Boolean>>()
    val isTrackInPlaylist: LiveData<Pair<String, Boolean>> = _isTrackInPlaylist

   fun handleLike(favIds: List<Long>, trackId: Long){
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

    fun render() {
        when (playerState.value) {
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
                    while (playerState.value == PLAYING) {
                        delay(300)
                        playerInteractor.getCurrentTime().collect() {
                            renderTime(it)
                        }
                    }
                }
                _screenState.value = PlayerScreenState.PlayButtonHandling(playerState.value!!)
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

    fun onResume() {
        when (playerState.value) {

            DEFAULT -> {
            }
            PAUSED -> {
                _screenState.value = PlayerScreenState.BeginningState(playerTrack)
                _screenState.value = PlayerScreenState.Preparing
                timerJob?.cancel()
                timerJob = viewModelScope.launch {
                    playerInteractor.getCurrentTime().collect() {
                        renderTime(it)
                    }
                }
            }
            PLAYING -> {
                _screenState.value = PlayerScreenState.BeginningState(playerTrack)
                _screenState.value = PlayerScreenState.Preparing

            }
            COMPLETED, PREPARED -> {
                _screenState.value = PlayerScreenState.BeginningState(playerTrack)
                _screenState.value = PlayerScreenState.Preparing
                _screenState.value = PlayerScreenState.PlayCompleting
            }
            else -> {}
        }
    }


    fun onStop() {
        playerInteractor.pause()
    }

    private fun renderTime(time: Int) {
        _screenState.value = PlayerScreenState.Playing(parseTime(time))
    }

    fun playbackControl() {
        when (playerState.value) {
            PLAYING -> {
                pause()
            }
            PREPARED, PAUSED, COMPLETED -> {
                start()
            }
            else -> {}
        }
    }

    private fun start() {
        playerInteractor.start()
    }

    private fun pause() {
        playerInteractor.pause()
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

    fun onDestroy() {
        timerJob?.cancel()
        playerInteractor.onDestroy()
    }

    private fun parseTime(_time: Int): String {
        return formatTime(_time)
    }
}


