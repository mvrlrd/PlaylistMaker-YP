package ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.PlaylistEntity
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.PlaylistTrackCrossRef
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.TrackEntity

@Database(version = 3, entities = [PlaylistEntity::class, PlaylistTrackCrossRef::class, TrackEntity::class])
abstract class PlaylistDb : RoomDatabase() {
    abstract fun getDao(): PlaylistDao
}