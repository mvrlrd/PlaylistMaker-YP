package ru.mvrlrd.playlistmaker.search.data


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

override fun searchTracks(query: String): Flow<Resource<List<Track>>> = flow {
    val response = networkClient.doRequest(TracksSearchRequest(query))
    when (response.resultCode) {
        CONNECTION_ERROR -> {
            emit(
                Resource.Error(
                    responseCode = response.resultCode,
                    errorMessage = response.errorMessage
                )
            )
        }
        SUCCESS_CODE -> {
            with(response as TracksSearchResponse) {
                val data = results.map { it.mapToTrack() }
                emit(Resource.Success(responseCode = response.resultCode, data = data))
            }
        }
        else -> {
            emit(Resource.Error(responseCode = response.resultCode, errorMessage = response.errorMessage))
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
        const val CONNECTION_ERROR = -1
    }
}

