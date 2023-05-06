package ru.mvrlrd.playlistmaker.settings.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.settings.ui.SettingsViewModel

val viewModelModule = module {
    viewModel {
        SettingsViewModel(themeSwitcherInteractor = get())
    }
}
