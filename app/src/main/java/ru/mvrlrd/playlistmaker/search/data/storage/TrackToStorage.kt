package ru.mvrlrd.playlistmaker.search.data.storage

data class TrackToStorage(
    val trackId: Long,
    val trackName: String? = "",
    val artistName: String? = "",
    val trackTime: String? = "",
    val image: String? = "",
    val image60: String? = "",
    val album: String? = "",
    val year: String? = "",
    val genre: String? = "",
    val country: String? = "",
    val previewUrl: String? = "",
)
