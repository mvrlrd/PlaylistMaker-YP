package ru.mvrlrd.playlistmaker.player.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack

interface PlayerClient {

     fun getIff():LiveData<MyMediaPlayer.PlayerState>
     fun preparePlayer(playerTrack: PlayerTrack)

     fun setOnCompletionListener()

      fun getLiveTime():LiveData<Int>

     fun start()

     fun pause()

     fun onDestroy()

     fun getCurrentTime(): Flow<Int>
}