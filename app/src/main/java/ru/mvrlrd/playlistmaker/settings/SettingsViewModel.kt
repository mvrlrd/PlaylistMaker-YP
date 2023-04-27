package ru.mvrlrd.playlistmaker.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mvrlrd.playlistmaker.ThemeSwitcher

class SettingsViewModel(val application: ThemeSwitcher) : ViewModel() {
    fun switchTheme(isChecked: Boolean) {
        application.switchTheme(isChecked)
    }

    fun isDarkThemeOn(): Boolean {
        return application.isDarkModeOn()
    }

    companion object {
        fun getViewModelFactory(application: ThemeSwitcher): ViewModelProvider.Factory =
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