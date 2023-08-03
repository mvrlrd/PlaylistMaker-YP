package ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "trackId"], tableName = "playlist_song_cross_ref_table")
data class PlaylistTrackCrossRef(
    val playlistId: Long,
    val trackId: Long,
    val date: Long
)