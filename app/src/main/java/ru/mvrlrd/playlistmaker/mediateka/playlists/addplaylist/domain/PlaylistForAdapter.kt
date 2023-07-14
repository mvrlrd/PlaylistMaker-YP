package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain

data class PlaylistForAdapter(
    val playlistId: Int?=null,
    val name: String,
    val description: String,
    val playlistImagePath: String,
    var tracksQuantity: Int =32
)
