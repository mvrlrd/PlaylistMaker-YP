package ru.mvrlrd.playlistmaker.mediateka.favorites.data.favs_db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 3, entities = [TrackEntity::class])
abstract class FavoriteDb: RoomDatabase() {
    abstract fun getDao(): FavoriteDao
}