package ru.mvrlrd.playlistmaker.player.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter

interface PlayerInteractor {
    fun start()
    fun pause()
    fun onDestroy()
    fun setOnCompletionListener(onComplete: ()->Unit)
    fun getCurrentTime(): Int
    fun preparePlayer(playerTrack: PlayerTrack, prepare: () -> Unit)

    suspend fun addTrackToFavorite(playerTrack: PlayerTrack)

    suspend fun removeTrackFromFavorite(trackId: Int)

    fun getAllPlaylists(): Flow<List<PlaylistForAdapter>>

}