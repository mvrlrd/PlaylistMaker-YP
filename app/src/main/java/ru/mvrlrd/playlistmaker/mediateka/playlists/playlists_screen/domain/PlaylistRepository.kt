package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter

interface PlaylistRepository {
    fun getAllPlaylistsWithSongs(): Flow<List<PlaylistForAdapter>>
}