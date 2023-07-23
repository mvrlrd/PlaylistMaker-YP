package ru.mvrlrd.playlistmaker.player.data

import android.media.MediaPlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack

class MyMediaPlayer(private val mediaPlayer: MediaPlayer) : PlayerClient {

     private val _playerState = MutableStateFlow(PlayerState.DEFAULT)
        val playerState: StateFlow<PlayerState> get() = _playerState
    override fun getLivePlayerState(): Flow<PlayerState> {
        return playerState
    }

    override fun preparePlayer(playerTrack: PlayerTrack) {
        try {
            mediaPlayer.setDataSource(playerTrack.previewUrl)
            mediaPlayer.setOnPreparedListener {
                _playerState.value =  PlayerState.PREPARED
            }
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnCompletionListener {
                _playerState.value = PlayerState.COMPLETED
            }
        } catch (e: Exception) {
            _playerState.value = PlayerState.ERROR
        }
    }

    override fun start() {
        mediaPlayer.start()
        _playerState.value= PlayerState.PLAYING
    }

    override fun handleStartAndPause() {
        when (playerState.value) {
            PlayerState.PLAYING -> {
                pause()
            }
            PlayerState.PREPARED,
            PlayerState.PAUSED,
            PlayerState.COMPLETED,
            PlayerState.STOPPED -> {
                start()
            }
            else -> {}
        }
    }

    override fun pause() {
        if (mediaPlayer.isPlaying) {
            _playerState.value = PlayerState.PAUSED
            mediaPlayer.pause()
        }
    }

    override fun stopIt(){
        pause()
        _playerState.value = PlayerState.STOPPED
    }

    override fun onDestroy() {
        mediaPlayer.reset()
        _playerState.value = PlayerState.DEFAULT
    }

    override fun getCurrentTime(): Flow<Int> {
        return flow {
            emit(mediaPlayer.currentPosition)
        }
    }

    enum class PlayerState {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED,
        COMPLETED,
        ERROR,
        STOPPED
    }
}

