package ru.mvrlrd.playlistmaker.search.data

import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

interface ILocalStorage {
    fun addToHistory(trackForAdapter: TrackForAdapter)
    fun clearHistory()
    fun getHistory(): List<TrackForAdapter>
}