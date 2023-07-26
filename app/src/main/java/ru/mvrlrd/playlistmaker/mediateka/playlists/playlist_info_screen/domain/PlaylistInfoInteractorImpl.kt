package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.Song
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

class PlaylistInfoInteractorImpl(private val repository: PlaylistInfoRepository) : PlaylistInfoInteractor{
    override fun getPlaylist(id: Long): Flow<PlaylistInfo> {
        return repository.getPlaylistWithSongs(id)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long): Flow<Int> {
        return repository.removeTrackFromPlaylist(trackId, playlistId)
    }

    override fun getAllSongsForDebug(): Flow<List<Song>> {
        return repository.getAllSongsForDebug()
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        repository.deletePlaylist(playlistId)
    }

    override suspend fun getTracksByDescDate(playlistId: Long): List<TrackForAdapter>{
        return repository.getTrackListByDescDate(playlistId)
    }
}