package ru.mvrlrd.playlistmaker.player.domain

import java.io.Serializable

data class TrackForPlayer(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTime: String?,
    val image: String,
    val album: String,
    val year: String?,
    val genre: String,
    val country: String,
    val previewUrl: String?
) : Serializable {

    override fun equals(other: Any?): Boolean {
        return if (other !is TrackForPlayer) {
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