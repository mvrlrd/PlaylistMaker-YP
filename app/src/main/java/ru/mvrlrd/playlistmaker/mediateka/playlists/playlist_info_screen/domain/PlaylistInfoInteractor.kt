package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistInfoInteractor {
    fun getPlaylist(id: Long): Flow<PlaylistInfo>

    fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long):Flow<Int>
}