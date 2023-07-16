package ru.mvrlrd.playlistmaker.db_playlist.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song(
    @PrimaryKey(autoGenerate = true)
    val songId: Long
)