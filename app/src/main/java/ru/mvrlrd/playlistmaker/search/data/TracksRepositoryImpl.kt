package ru.mvrlrd.playlistmaker.search.data


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mvrlrd.playlistmaker.mediateka.favorites.data.favs_db.FavoriteDb
import ru.mvrlrd.playlistmaker.search.data.network.TracksSearchResponse
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter
import ru.mvrlrd.playlistmaker.search.domain.TracksRepository
import ru.mvrlrd.playlistmaker.search.util.Resource
import ru.mvrlrd.playlistmaker.search.data.model.*

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: ILocalStorage,
    private val favoriteDb: FavoriteDb
) :
    TracksRepository {

    override fun searchTracks(query: String): Flow<Resource<List<TrackForAdapter>>> = flow {
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
                    emit(Resource.Success(responseCode = response.resultCode, data = data))
                }
            }
            else -> {
                emit(
                    Resource.Error(
                        responseCode = response.resultCode,
                        errorMessage = response.errorMessage
                    )
                )
            }
        }
    }

    override fun addTrackToHistory(trackForAdapter: TrackForAdapter) {
        localStorage.addToHistory(trackForAdapter)
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }

    override suspend fun getHistory(): Flow<List<TrackForAdapter>> = flow {
        emit(localStorage.getHistory())
    }

    override suspend fun getFavIds(): Flow<List<Long>> {
        return flow {
            emit(favoriteDb.getDao().getFavoriteTrackIds())
        }
    }

    companion object {
        const val SUCCESS_CODE = 200
        const val CONNECTION_ERROR = -1
    }
}

