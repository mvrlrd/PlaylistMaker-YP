package ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistDb

val playlistsDataModule = module {
    single {
        Room.databaseBuilder(androidContext(), PlaylistDb::class.java, "playlist_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    factory {
        PlaylistConverter()
    }
}