package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain

import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter


data class PlaylistInfo(
    val playlist: PlaylistForAdapter,
    val songs: List<TrackForAdapter>
)

