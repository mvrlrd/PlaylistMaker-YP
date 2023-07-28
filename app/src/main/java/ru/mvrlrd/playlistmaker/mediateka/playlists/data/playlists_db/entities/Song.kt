package ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.mvrlrd.playlistmaker.player.util.formatTime

@Entity(tableName = "song_table")
data class Song(
    @PrimaryKey(autoGenerate = true)
    val songId: Long,
    val trackName: String? = "",
    val artistName: String? = "",
    val trackTime: String? = "",
    val image: String? = "",
    val image60: String? = "",
    val album: String? = "",
    val year: String? = "",
    val genre: String? = "",
    val country: String? = "",
    val previewUrl: String? = "",
    val date: Long
)
