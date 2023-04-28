package ru.mvrlrd.playlistmaker.settings.ui

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mvrlrd.playlistmaker.App

class SettingsViewModel(application: App) : AndroidViewModel(application) {
    private val switchThemeInteractor = application.themeSwitcherInteractor
    fun switchTheme(isChecked: Boolean) {
        switchThemeInteractor.switch(isChecked)
    }
    fun isDarkThemeOn(): Boolean {
        return switchThemeInteractor.isDarkModeOn()
    }
    companion object {
        fun getViewModelFactory(application: App): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(
                        application = application
                    ) as T
                }
            }
    }
}