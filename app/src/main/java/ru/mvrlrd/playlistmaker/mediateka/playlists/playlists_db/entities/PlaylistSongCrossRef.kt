package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_db.entities

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "songId"])
data class PlaylistSongCrossRef(
    val playlistId: Long,
    val songId: Long
)