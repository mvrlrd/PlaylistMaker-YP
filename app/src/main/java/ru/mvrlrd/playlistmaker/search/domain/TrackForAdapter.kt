package ru.mvrlrd.playlistmaker.search.domain

import ru.mvrlrd.playlistmaker.player.util.formatTime
import java.io.Serializable

data class TrackForAdapter(
    val trackId: Long,
    val trackName: String? = "",
    val artistName: String? = "",
    var trackTime: String? = "",
    val image: String? = "",
    val image60: String? = "",
    val album: String? = "",
    var year: String? = "",
    val genre: String? = "",
    val country: String? = "",
    var previewUrl: String? = "",
    var isFavorite: Boolean = false
) : Serializable {

    override fun equals(other: Any?): Boolean {
        return if (other !is TrackForAdapter) {
            false
        } else {
            other.trackId == this.trackId
        }
    }

    override fun hashCode(): Int {
        return trackId.toInt()
    }

    override fun toString(): String {
        return "${this.artistName} - ${this.trackName} (${formatTime(this.trackTime!!.toInt())})"
    }
}