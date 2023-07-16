package ru.mvrlrd.playlistmaker.playlistDb.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mvrlrd.playlistmaker.playlistDb.PlaylistEntity
import ru.mvrlrd.playlistmaker.playlistDb.data.entities.PlaylistSongCrossRef
import ru.mvrlrd.playlistmaker.playlistDb.data.entities.Song

@Database(version = 2, entities = [PlaylistEntity::class, PlaylistSongCrossRef::class, Song::class])
abstract class PlaylistDb: RoomDatabase() {
    abstract fun getDao(): PlaylistDao
}