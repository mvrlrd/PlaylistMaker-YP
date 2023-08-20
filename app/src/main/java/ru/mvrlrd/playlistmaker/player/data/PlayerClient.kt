package ru.mvrlrd.playlistmaker.player.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack

interface PlayerClient {
    fun getLivePlayerState(): StateFlow<MyMediaPlayer.PlayerState>
    fun preparePlayer(playerTrack: PlayerTrack)
    fun start()
    fun pause()
    fun onDestroy()
    fun getCurrentTime(): Flow<Int>

    fun stopIt()

    fun handleStartAndPause()

    suspend fun prp(playerTrack: PlayerTrack)
}