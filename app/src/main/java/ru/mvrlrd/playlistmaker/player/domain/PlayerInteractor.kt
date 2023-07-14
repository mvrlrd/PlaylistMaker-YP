package ru.mvrlrd.playlistmaker.player.domain

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer

interface PlayerInteractor {
    fun start()
    fun pause()
    fun onDestroy()
    fun setOnCompletionListener()
    fun getCurrentTime(): Flow<Int>
    fun preparePlayer(playerTrack: PlayerTrack)

    suspend fun addTrackToFavorite(playerTrack: PlayerTrack)

     fun getLiveTime():LiveData<Int>

    suspend fun removeTrackFromFavorite(trackId: Int)

    fun getAllPlaylists(): Flow<List<PlaylistForAdapter>>
    fun getIff():LiveData<MyMediaPlayer.PlayerState>
}