package ru.mvrlrd.playlistmaker.player.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter

interface PlayerRepository {
    fun preparePlayer(playerTrack: PlayerTrack, prepare: () -> Unit)

    fun setOnCompletionListener(onComplete: () -> Unit)

    fun start()

    fun pause()

    fun onDestroy()

    fun getCurrentTime(): Int

    suspend fun addToFavorite(playerTrack: PlayerTrack)


    suspend fun removeFromFavorite(trackId: Int)

    fun getAllPlaylists(): Flow<List<PlaylistForAdapter>>

}