package ru.mvrlrd.playlistmaker.settings.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.settings.data.ThemeSwitchRepositoryImpl
import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchRepository

val settingsRepositoryModule = module {
    single<ThemeSwitchRepository> {
        ThemeSwitchRepositoryImpl(themeStorage = get())
    }
}