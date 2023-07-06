package ru.mvrlrd.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.search.util.Resource

interface TracksRepository {
    fun searchTracks(query: String): Flow<Resource<List<AdapterTrack>>>
    fun addTrackToHistory(adapterTrack: AdapterTrack)
    fun clearHistory()
    suspend fun getHistory(): Flow<List<AdapterTrack>>
   suspend fun getFavIds(): Flow<List<Int>>
}