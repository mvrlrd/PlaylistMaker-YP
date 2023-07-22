package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter

interface PlaylistInteractor {
    fun getAllPlaylist(): Flow<List<PlaylistForAdapter>>

}