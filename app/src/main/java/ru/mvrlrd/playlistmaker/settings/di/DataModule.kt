package ru.mvrlrd.playlistmaker.settings.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.settings.data.IThemeStorage
import ru.mvrlrd.playlistmaker.settings.data.storage.ThemeStorage

val dataModule = module {
    single<IThemeStorage>{
        ThemeStorage(sharedPreferences = get())
    }
    single<SharedPreferences> {
        androidContext().getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
    }
}

private const val THEME_PREFERENCES = "current_theme"