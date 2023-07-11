package ru.mvrlrd.playlistmaker.search.data.storage

data class TrackToStorage(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val image: String,
    val album: String,
    val year: String,
    val genre: String,
    val country: String,
    val previewUrl: String,

)
