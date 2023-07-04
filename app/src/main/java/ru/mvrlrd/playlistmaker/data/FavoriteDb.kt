package ru.mvrlrd.playlistmaker.data

import androidx.room.Database

@Database(version = 1, entities = [TrackEntity::class])
abstract class FavoriteDb {
    abstract fun getDao(): FavoriteDao
}