package ru.mvrlrd.playlistmaker.database.data.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.favorites.data.favs_db.FavoriteDb
import ru.mvrlrd.playlistmaker.mediateka.favorites.data.favs_db.TrackConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistDb

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