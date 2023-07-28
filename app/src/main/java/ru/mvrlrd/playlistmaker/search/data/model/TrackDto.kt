package ru.mvrlrd.playlistmaker.search.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class TrackDto(
    val trackId: Long,
    val trackName: String? = "",
    val artistName: String? = "",
    @SerializedName("trackTimeMillis") val trackTime: String? = "",
    @SerializedName("artworkUrl100") val image: String? = "",
    @SerializedName("artworkUrl60") val image60: String? = "",
    @SerializedName("collectionName") val album: String? = "",
    @SerializedName("releaseDate") val year: String? = "",
    @SerializedName("primaryGenreName") val genre: String? = "",
    val country: String? = "",
    val previewUrl: String? = ""
) : Serializable {

    override fun equals(other: Any?): Boolean {
        return if (other !is TrackDto) {
            false
        } else {
            other.trackId == trackId
        }
    }

    override fun hashCode(): Int {
        return trackId.toInt()
    }
}

