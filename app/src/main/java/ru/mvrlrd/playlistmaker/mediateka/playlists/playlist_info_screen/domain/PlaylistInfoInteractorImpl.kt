package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.TrackEntity
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

class PlaylistInfoInteractorImpl(private val repository: PlaylistInfoRepository) : PlaylistInfoInteractor{
    override fun getPlaylistInfo(id: Long): Flow<PlaylistInfo> {
        return repository.getPlaylistInfo(id)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long): Flow<Int> {
        return repository.deleteTrackFromPlaylist(trackId, playlistId)
    }

    override fun getAllTracksForDebugging(): Flow<List<TrackEntity>> {
        return repository.getAllTacksForDebugging()
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        repository.deletePlaylist(playlistId)
    }

    override suspend fun getTracksByDescDate(playlistId: Long): List<TrackForAdapter>{
        return repository.getTracksByDescDate(playlistId)
    }
}