package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song(
    @PrimaryKey(autoGenerate = true)
    val songId: Long
)