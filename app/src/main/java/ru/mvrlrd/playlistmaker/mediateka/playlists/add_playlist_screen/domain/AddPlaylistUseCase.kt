package ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.domain

import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter

interface AddPlaylistUseCase {
    suspend fun addPlaylist(playlist: PlaylistForAdapter)
}