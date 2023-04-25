package ru.mvrlrd.playlistmaker.data

import ru.mvrlrd.playlistmaker.domain.Track

interface ILocalStorage {
    fun addToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}