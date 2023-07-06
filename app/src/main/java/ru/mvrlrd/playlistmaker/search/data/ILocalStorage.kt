package ru.mvrlrd.playlistmaker.search.data

import ru.mvrlrd.playlistmaker.search.domain.AdapterTrack

interface ILocalStorage {
    fun addToHistory(adapterTrack: AdapterTrack)
    fun clearHistory()
    fun getHistory(): List<AdapterTrack>
}