package ru.mvrlrd.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.search.util.Resource

interface TracksRepository {
    fun searchTracks(query: String): Flow<Resource<List<Track>>>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    suspend fun getHistory(): Flow<List<Track>>
   suspend fun getFavIds(): Flow<List<Int>>
}