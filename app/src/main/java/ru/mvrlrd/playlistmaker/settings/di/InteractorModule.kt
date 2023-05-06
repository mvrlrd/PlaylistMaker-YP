package ru.mvrlrd.playlistmaker.settings.di

import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchInteractor
import ru.mvrlrd.playlistmaker.settings.domain.impl.ThemeSwitchInteractorImpl

val interactorModule = module {
    single<ThemeSwitchInteractor>{
        ThemeSwitchInteractorImpl(themeSwitchRepository = get())
    }
}