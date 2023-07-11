package ru.mvrlrd.playlistmaker.player.domain

import java.io.Serializable

data class PlayerTrack(
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
        return if (other !is PlayerTrack) {
            false
        } else {
            other.trackId == trackId
        }
    }

    override fun hashCode(): Int {
        return trackId
    }

    fun getCoverArtwork() = image.replaceAfterLast('/', "512x512bb.jpg")
}