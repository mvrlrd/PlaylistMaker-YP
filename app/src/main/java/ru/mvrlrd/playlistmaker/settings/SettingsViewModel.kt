package ru.mvrlrd.playlistmaker.settings

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mvrlrd.playlistmaker.App

class SettingsViewModel(val application: App): AndroidViewModel(application) {

    fun switchTheme(isChecked: Boolean){
        application.switchTheme(isChecked)
    }

     fun isDarkThemeOn(): Boolean {
        return application.isDarkModeOn()
    }

    fun applyTheme(){
        application.applySavedThemeMode()
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