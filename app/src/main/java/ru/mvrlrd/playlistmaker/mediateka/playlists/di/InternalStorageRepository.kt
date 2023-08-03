package ru.mvrlrd.playlistmaker.mediateka.playlists.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.InternalStorageRepositoryImpl
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.InternalStorageRepository

val internalStorageRepositoryModule = module {
    factory<InternalStorageRepository> { InternalStorageRepositoryImpl(androidContext()) }
}