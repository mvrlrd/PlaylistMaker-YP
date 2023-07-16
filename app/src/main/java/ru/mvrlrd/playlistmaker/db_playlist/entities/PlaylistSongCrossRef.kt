package ru.mvrlrd.playlistmaker.db_playlist.data.entities

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "songId"])
data class PlaylistSongCrossRef(
    val playlistId: Int,
    val songId: Int
)