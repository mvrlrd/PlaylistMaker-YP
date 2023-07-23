package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain

import kotlinx.coroutines.flow.Flow

class PlaylistInfoInteractorImpl(private val repository: PlaylistInfoRepository) : PlaylistInfoInteractor{
    override fun getPlaylist(id: Long): Flow<PlaylistInfo> {
        return repository.getPlaylistWithSongs(id)
    }

    override fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long): Flow<Int> {
        return repository.removeTrackFromPlaylist(trackId, playlistId)
    }
}