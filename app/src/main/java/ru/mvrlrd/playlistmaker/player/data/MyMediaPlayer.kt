package ru.mvrlrd.playlistmaker.player.data

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack

class MyMediaPlayer(private val mediaPlayer: MediaPlayer) : PlayerClient {
    private var _playerState = MutableLiveData<PlayerState>()
    private val playerState: LiveData<PlayerState> = _playerState
    override fun getLivePlayerState(): LiveData<PlayerState> {
        return playerState
    }

    init {
        _playerState.value = PlayerState.DEFAULT
    }

    override fun preparePlayer(playerTrack: PlayerTrack) {
        try {
            mediaPlayer.setDataSource(playerTrack.previewUrl)
            mediaPlayer.setOnPreparedListener {
                _playerState.value = PlayerState.PREPARED
            }
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnCompletionListener {
                _playerState.postValue(PlayerState.COMPLETED)
            }
        } catch (e: Exception) {
            _playerState.value = PlayerState.ERROR
        }
    }

    override fun start() {
        mediaPlayer.start()
        _playerState.value = PlayerState.PLAYING
    }

    override fun pause() {
        if (mediaPlayer.isPlaying) {
            _playerState.value = PlayerState.PAUSED
            mediaPlayer.pause()
        }
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
        ERROR
    }
}

