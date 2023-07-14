package ru.mvrlrd.playlistmaker.player.data

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack

class MyMediaPlayer(private val mediaPlayer: MediaPlayer): PlayerClient {
    private var _playerState =  MutableLiveData<PlayerState> ()
    private val live : LiveData<PlayerState> =_playerState

    private var _time =  MutableLiveData<Int> ()
    val time : LiveData<Int> =_time

    override fun getIff():LiveData<PlayerState>{
        return live
    }

    override fun getLiveTime():LiveData<Int>{
         _time.value = mediaPlayer.currentPosition
        return time
    }

    init {
        _playerState.value = PlayerState.DEFAULT
    }
    override fun preparePlayer(playerTrack: PlayerTrack) {
        mediaPlayer.setDataSource(playerTrack.previewUrl)
        mediaPlayer.prepareAsync()
        setOnCompletionListener()
        mediaPlayer.setOnPreparedListener {
            _playerState.value = PlayerState.PREPARED
        }
    }

    override fun setOnCompletionListener() {
        mediaPlayer.setOnCompletionListener {
            _playerState.postValue(PlayerState.COMPLETED)
        }

    }

    override fun start() {
        mediaPlayer.start()
        _playerState.value = PlayerState.PLAYING
    }

    override fun pause() {
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            _playerState.value = PlayerState.PAUSED
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
    }
}

