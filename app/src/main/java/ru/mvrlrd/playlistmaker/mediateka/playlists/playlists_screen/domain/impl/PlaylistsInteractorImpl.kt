package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.domain.PlaylistInteractor
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.domain.PlaylistRepository

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override fun getAllPlaylist(): Flow<List<PlaylistForAdapter>> {
        return repository.getAllPlaylistsWithSongs()
    }
}