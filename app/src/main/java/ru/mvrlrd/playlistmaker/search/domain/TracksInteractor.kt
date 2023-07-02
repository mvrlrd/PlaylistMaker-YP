package ru.mvrlrd.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.search.util.Resource


interface TracksInteractor {
    fun searchTracks(query: String): Flow<Resource<List<Track>>>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}

