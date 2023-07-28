package ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.PlaylistEntity
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.PlaylistSongCrossRef
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.TrackEntity

data class SongWithPlaylists(
    @Embedded val trackEntity: TrackEntity,
    @Relation(
        parentColumn = "trackId",
        entityColumn = "playlistId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val playlists: List<PlaylistEntity>
)
