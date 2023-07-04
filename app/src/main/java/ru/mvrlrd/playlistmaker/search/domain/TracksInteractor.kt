package ru.mvrlrd.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow


interface TracksInteractor {
    fun searchTracks(query: String): Flow<Pair<List<Track>?, Pair<Int, String?>>>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    suspend fun getHistory(): Flow<List<Track>>
}

