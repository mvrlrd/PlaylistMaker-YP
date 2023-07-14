package ru.mvrlrd.playlistmaker.playlistDb.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mvrlrd.playlistmaker.playlistDb.PlaylistEntity

@Database(version = 1, entities = [PlaylistEntity::class])
abstract class PlaylistDb: RoomDatabase() {
    abstract fun getDao(): PlaylistDao
}