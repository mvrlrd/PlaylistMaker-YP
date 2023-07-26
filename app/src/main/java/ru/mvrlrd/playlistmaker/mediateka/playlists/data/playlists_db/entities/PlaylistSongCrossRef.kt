package ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "songId"], tableName = "playlist_song_cross_ref_table")
data class PlaylistSongCrossRef(
    val playlistId: Long,
    val songId: Long,
    val date: Long
)