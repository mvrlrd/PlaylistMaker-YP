package ru.mvrlrd.playlistmaker.database.data.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.database.data.FavoriteDb
import ru.mvrlrd.playlistmaker.database.data.TrackConverter
import ru.mvrlrd.playlistmaker.db_playlist.data.PlaylistConverter
import ru.mvrlrd.playlistmaker.db_playlist.data.PlaylistDb

val dataBaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), FavoriteDb::class.java, "fav_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory {
        TrackConverter()
    }

    single {
        Room.databaseBuilder(androidContext(), PlaylistDb::class.java, "playlist_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory {
        PlaylistConverter()
    }
}