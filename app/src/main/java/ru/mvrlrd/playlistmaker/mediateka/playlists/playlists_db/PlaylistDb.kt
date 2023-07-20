package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_db.entities.PlaylistEntity
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_db.entities.PlaylistSongCrossRef
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_db.entities.Song

@Database(version = 2, entities = [PlaylistEntity::class, PlaylistSongCrossRef::class, Song::class])
abstract class PlaylistDb : RoomDatabase() {
    abstract fun getDao(): PlaylistDao
}