package ru.mvrlrd.playlistmaker.search.data


import ru.mvrlrd.playlistmaker.search.data.network.TracksSearchResponse
import ru.mvrlrd.playlistmaker.search.domain.Track
import ru.mvrlrd.playlistmaker.search.domain.TracksRepository
import ru.mvrlrd.playlistmaker.search.util.Resource
import ru.mvrlrd.playlistmaker.search.data.model.*

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: ILocalStorage
) :
    TracksRepository {
    override fun searchTracks(query: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(query))

        return when (response.resultCode) {
            SUCCESS_CODE -> {
                Resource.Success((response as TracksSearchResponse).results.map {
                    it.mapToTrack()
                }, responseCode = response.resultCode)
            }
            else -> {
                Resource.Error(responseCode = response.resultCode, message = response.errorMessage)
            }
        }
    }

    override fun addTrackToHistory(track: Track) {
        localStorage.addToHistory(track)
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }

    override fun getHistory(): List<Track> {
        return localStorage.getHistory()
    }
    companion object{
        const val SUCCESS_CODE = 200
    }
}

