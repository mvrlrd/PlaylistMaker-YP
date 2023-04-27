package ru.mvrlrd.playlistmaker.settings.data.storage

import android.content.SharedPreferences
import ru.mvrlrd.playlistmaker.settings.data.IThemeStorage

class ThemeStorage(private val sharedPreferences: SharedPreferences): IThemeStorage{

    override fun switch(darkThemeEnabled: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(IS_DARK_MODE_ON, darkThemeEnabled)
            .apply()
    }

    override fun isDarkModeOn(): Boolean {
        return sharedPreferences.getBoolean(IS_DARK_MODE_ON, false)
    }

    companion object{
        private const val IS_DARK_MODE_ON = "is_dark_mode_on"
    }
}