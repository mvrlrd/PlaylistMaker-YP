package ru.mvrlrd.playlistmaker.playlistDb.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.mvrlrd.playlistmaker.playlistDb.PlaylistEntity
import ru.mvrlrd.playlistmaker.playlistDb.data.entities.PlaylistSongCrossRef
import ru.mvrlrd.playlistmaker.playlistDb.data.entities.Song

data class PlaylistWithSongs(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "songId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val songs: List<Song>
)