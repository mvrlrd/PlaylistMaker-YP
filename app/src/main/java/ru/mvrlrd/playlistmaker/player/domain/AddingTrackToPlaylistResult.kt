package ru.mvrlrd.playlistmaker.player.domain

data class AddingTrackToPlaylistResult(
    val playlistName: String,
    val wasAdded: Boolean
)
