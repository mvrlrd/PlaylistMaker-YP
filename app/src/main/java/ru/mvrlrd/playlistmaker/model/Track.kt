package ru.mvrlrd.playlistmaker.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: String,
    @SerializedName("artworkUrl100") val image: String,
    @SerializedName("collectionName")val album: String,
    @SerializedName("releaseDate")val year: String,
    @SerializedName("primaryGenreName")val genre: String,
    val country: String
): Serializable {

    override fun equals(other: Any?): Boolean {
        return if (other !is Track){
            false
        }else{
            other.trackId == trackId
        }
    }

    override fun hashCode(): Int {
        return trackId
    }
}

