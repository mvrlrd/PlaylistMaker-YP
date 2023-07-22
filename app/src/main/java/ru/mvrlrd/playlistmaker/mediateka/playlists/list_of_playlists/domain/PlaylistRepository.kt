package ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getAllPlaylistsWithSongs(): Flow<List<PlaylistForAdapter>>
}