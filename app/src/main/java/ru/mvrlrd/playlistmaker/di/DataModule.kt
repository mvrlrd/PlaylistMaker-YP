package ru.mvrlrd.playlistmaker.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.data.FavoriteDb

val dataBaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), FavoriteDb::class.java, "fav_database.db")
            .build()
    }
}