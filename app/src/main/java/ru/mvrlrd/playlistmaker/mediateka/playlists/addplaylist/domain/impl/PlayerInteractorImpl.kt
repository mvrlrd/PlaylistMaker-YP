package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistInteractor
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistRepository

class PlaylistInteractorImpl(private val repository: PlaylistRepository): PlaylistInteractor {
    override fun getAllPlaylist(): Flow<List<PlaylistForAdapter>> {
        return repository.getAllPlaylistsWithSongs()
    }

    override suspend fun addPlaylist(playlistForAdapter: PlaylistForAdapter) {
        repository.insertPlaylist(playlistForAdapter)
    }
}