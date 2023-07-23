package ru.mvrlrd.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow


interface TracksInteractor {
    fun searchTracks(query: String): Flow<Pair<List<TrackForAdapter>?, Pair<Int, String?>>>
    fun addTrackToHistory(trackForAdapter: TrackForAdapter)
    fun clearHistory()
    suspend fun getHistory(): Flow<List<TrackForAdapter>>
}

