package ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.PlaylistEntity
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.PlaylistTrackCrossRef
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.TrackEntity

data class TrackWithPlaylists(
    @Embedded val trackEntity: TrackEntity,
    @Relation(
        parentColumn = "trackId",
        entityColumn = "playlistId",
        associateBy = Junction(PlaylistTrackCrossRef::class)
    )
    val playlists: List<PlaylistEntity>
)
