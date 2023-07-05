package ru.mvrlrd.playlistmaker.search.data


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mvrlrd.playlistmaker.favorites.data.FavoriteDb
import ru.mvrlrd.playlistmaker.search.data.network.TracksSearchResponse
import ru.mvrlrd.playlistmaker.search.domain.Track
import ru.mvrlrd.playlistmaker.search.domain.TracksRepository
import ru.mvrlrd.playlistmaker.search.util.Resource
import ru.mvrlrd.playlistmaker.search.data.model.*

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: ILocalStorage,
    private val favoriteDb: FavoriteDb
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
                val favoriteIds = favoriteDb.getDao().getFavoriteTrackIds()
                val data = results.map { it.mapToTrack(favoriteIds.contains(it.trackId)) }
                println("___TracksRepositoryImpl___")
                data.filter { it.isFavorite==true }.forEach {
                    println(it.trackName)
                }
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

    override suspend fun getHistory(): Flow<List<Track>> = flow {
        val favoriteIds = favoriteDb.getDao().getFavoriteTrackIds()
        val historyTracks = localStorage.getHistory()
        historyTracks.forEach { it.isFavorite = (favoriteIds.contains(it.trackId)) }
        emit(historyTracks)
    }

    override suspend fun getFavIds(): Flow<List<Int>> {
        return flow {
           emit(favoriteDb.getDao().getFavoriteTrackIds())
        }
    }

    companion object{
        const val SUCCESS_CODE = 200
        const val CONNECTION_ERROR = -1
    }
}

