package ru.mvrlrd.playlistmaker.playlistDb.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song(
    @PrimaryKey val songId: Int,
    val songName: String,
    val artist: String
)