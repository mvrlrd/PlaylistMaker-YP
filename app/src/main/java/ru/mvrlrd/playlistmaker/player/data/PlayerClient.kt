package ru.mvrlrd.playlistmaker.player.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack

interface PlayerClient {
    fun getLivePlayerState(): Flow<MyMediaPlayer.PlayerState>
    fun preparePlayer(playerTrack: PlayerTrack)
    fun start()
    fun pause()
    fun onDestroy()
    fun getCurrentTime(): Flow<Int>

    fun stopIt()

    fun handleStartAndPause()
}