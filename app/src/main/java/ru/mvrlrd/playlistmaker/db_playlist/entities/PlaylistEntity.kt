package ru.mvrlrd.playlistmaker.db_playlist.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = -1,
    val name: String,
    val description: String,
    val playlistImagePath: String,
)







