package ru.mvrlrd.playlistmaker.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [TrackEntity::class])
abstract class FavoriteDb: RoomDatabase() {
    abstract fun getDao(): FavoriteDao
}