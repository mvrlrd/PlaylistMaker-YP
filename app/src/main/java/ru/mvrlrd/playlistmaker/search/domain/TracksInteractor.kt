package ru.mvrlrd.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow


interface TracksInteractor {
    fun searchTracks(query: String): Flow<Pair<List<AdapterTrack>?, Pair<Int, String?>>>
    fun addTrackToHistory(adapterTrack: AdapterTrack)
    fun clearHistory()
    suspend fun getHistory(): Flow<List<AdapterTrack>>
    suspend fun getFavIds():Flow<List<Long>>
}

