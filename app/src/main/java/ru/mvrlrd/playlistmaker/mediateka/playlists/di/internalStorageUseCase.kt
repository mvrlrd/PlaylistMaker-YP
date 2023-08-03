package ru.mvrlrd.playlistmaker.mediateka.playlists.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.GetInternalFileUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.GetInternalFileUseCaseImpl

val internalStorageUseCase = module {
    factory<GetInternalFileUseCase> { GetInternalFileUseCaseImpl(repository = get()) }
}