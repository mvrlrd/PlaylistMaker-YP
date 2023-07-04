package ru.mvrlrd.playlistmaker.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mvrlrd.playlistmaker.search.domain.Track
import ru.mvrlrd.playlistmaker.search.domain.TracksInteractor
import ru.mvrlrd.playlistmaker.search.domain.TracksRepository
import ru.mvrlrd.playlistmaker.search.util.Resource

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    override fun searchTracks(query: String) : Flow<Pair<List<Track>?, Pair<String, String>?>> {
        return repository.searchTracks(query).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message!! to result.responseCode.toString())
                }
            }
        }
    }

    override fun addTrackToHistory(track: Track) {
        repository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
    override fun getHistory(): List<Track> {
        return repository.getHistory()
    }
}