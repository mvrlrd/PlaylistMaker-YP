package ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.domain.PlaylistInteractor
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.domain.PlaylistRepository

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override fun getAllPlaylist(): Flow<List<PlaylistForAdapter>> {
        return repository.getAllPlaylistsWithSongs()
    }
}