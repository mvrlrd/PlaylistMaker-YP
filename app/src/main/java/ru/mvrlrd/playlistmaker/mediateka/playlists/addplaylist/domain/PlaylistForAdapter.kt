package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain

data class PlaylistForAdapter(
    val playlistId: Long? = null,
    val name: String,
    val description: String,
    val playlistImagePath: String,
    var tracksQuantity: Int = 0
)
