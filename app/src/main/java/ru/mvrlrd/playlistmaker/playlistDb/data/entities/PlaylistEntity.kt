package ru.mvrlrd.playlistmaker.playlistDb

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import ru.mvrlrd.playlistmaker.database.data.TrackEntity

@Entity(tableName = "playlist_table")
class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int = -1,
    val name: String,
    val description: String,
    val playlistImagePath: String,
)

//@Entity(primaryKeys = ["playlistId", "trackId"])
//data class PlaylistTrackCrossRef(
//    val playlistId: Int,
//    val trackId: Int
//)






