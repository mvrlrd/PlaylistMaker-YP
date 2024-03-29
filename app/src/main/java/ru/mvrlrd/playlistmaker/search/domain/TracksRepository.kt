package ru.mvrlrd.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.search.util.Resource

interface TracksRepository {
    fun searchTracks(query: String): Flow<Resource<List<TrackForAdapter>>>
    fun addTrackToHistory(trackForAdapter: TrackForAdapter)
    fun clearHistory()
    suspend fun getHistory(): Flow<List<TrackForAdapter>>
}