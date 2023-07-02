package ru.mvrlrd.playlistmaker.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mvrlrd.playlistmaker.search.domain.Track
import ru.mvrlrd.playlistmaker.search.domain.TracksInteractor
import ru.mvrlrd.playlistmaker.search.domain.TracksRepository
import ru.mvrlrd.playlistmaker.search.util.Resource

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    override fun searchTracks(query: String) : Flow<Resource<List<Track>>> {
        return repository.searchTracks(query).map { result ->
            when (result) {
                is Resource.Success -> {
                    Resource.Success(data = result.data!!, responseCode = 200)
                }
                is Resource.Error -> {
                    Resource.Error(message = result.message!!, responseCode = result.responseCode)
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