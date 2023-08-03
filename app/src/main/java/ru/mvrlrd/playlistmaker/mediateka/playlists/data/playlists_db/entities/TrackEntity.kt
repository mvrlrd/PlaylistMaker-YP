package ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val trackId: Long,
    val trackName: String? = "",
    val artistName: String? = "",
    val trackTime: String? = "",
    val image: String? = "",
    val album: String? = "",
    val year: String? = "",
    val genre: String? = "",
    val country: String? = "",
    val previewUrl: String? = "",
    val date: Long
)
