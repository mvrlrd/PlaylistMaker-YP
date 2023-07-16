package ru.mvrlrd.playlistmaker.database.data


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
data class TrackEntity(
    @PrimaryKey
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String?,
    val image: String,
    val album: String,
    val year: String?,
    val genre: String,
    val country: String,
    val previewUrl: String?,
    val date: Long
)


