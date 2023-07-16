package ru.mvrlrd.playlistmaker.db_playlist.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.mvrlrd.playlistmaker.db_playlist.entities.PlaylistEntity
import ru.mvrlrd.playlistmaker.db_playlist.entities.PlaylistSongCrossRef
import ru.mvrlrd.playlistmaker.db_playlist.entities.Song

data class PlaylistWithSongs(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "songId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val songs: List<Song>
)