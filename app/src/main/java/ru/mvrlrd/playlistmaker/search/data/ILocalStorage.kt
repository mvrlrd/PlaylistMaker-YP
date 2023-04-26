package ru.mvrlrd.playlistmaker.search.data

import ru.mvrlrd.playlistmaker.search.domain.Track

interface ILocalStorage {
    fun addToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}