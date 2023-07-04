package ru.mvrlrd.playlistmaker.search.di

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.search.ui.SearchViewModel

val searchViewModelModule = module {
    viewModel {
        SearchViewModel(tracksInteractor = get(), context = androidApplication())
    }
}