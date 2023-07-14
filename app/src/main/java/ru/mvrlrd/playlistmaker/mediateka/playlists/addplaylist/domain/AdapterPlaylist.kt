package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain

data class AdapterPlaylist(
    val playlistId: Int?=null,
    val name: String,
    val description: String,
    val playlistImagePath: String,
)
