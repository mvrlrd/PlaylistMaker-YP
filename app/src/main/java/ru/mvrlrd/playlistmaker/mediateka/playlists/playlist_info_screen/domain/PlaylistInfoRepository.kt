package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistInfoRepository {
    fun getPlaylistWithSongs(id: Long): Flow<PlaylistInfo>

     fun getFavsIds():Flow<List<Long>>
}