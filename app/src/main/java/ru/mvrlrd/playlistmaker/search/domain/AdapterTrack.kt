package ru.mvrlrd.playlistmaker.search.domain

import java.io.Serializable

data class AdapterTrack(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String?,
    val image: String,
    val album: String,
    val year: String?,
    val genre: String,
    val country: String,
    val previewUrl: String?,
    var isFavorite: Boolean = false
) : Serializable {

    override fun equals(other: Any?): Boolean {
        return if (other !is AdapterTrack) {
            false
        } else {
            other.trackId == this.trackId
        }
    }

    override fun hashCode(): Int {
        return trackId
    }
}