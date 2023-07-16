package ru.mvrlrd.playlistmaker.db_playlist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mvrlrd.playlistmaker.db_playlist.PlaylistEntity
import ru.mvrlrd.playlistmaker.db_playlist.data.entities.PlaylistSongCrossRef
import ru.mvrlrd.playlistmaker.db_playlist.data.entities.Song

@Database(version = 2, entities = [PlaylistEntity::class, PlaylistSongCrossRef::class, Song::class])
abstract class PlaylistDb: RoomDatabase() {
    abstract fun getDao(): PlaylistDao
}