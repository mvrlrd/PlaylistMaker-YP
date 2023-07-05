package ru.mvrlrd.playlistmaker.favorites.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 2, entities = [TrackEntity::class])
abstract class FavoriteDb: RoomDatabase() {
    abstract fun getDao(): FavoriteDao
}