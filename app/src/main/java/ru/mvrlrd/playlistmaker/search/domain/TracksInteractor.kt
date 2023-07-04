package ru.mvrlrd.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow


interface TracksInteractor {
    fun searchTracks(query: String): Flow<Pair<List<Track>?, Pair<String,String>?>>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}

