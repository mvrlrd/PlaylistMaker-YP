package ru.mvrlrd.playlistmaker.search.data

import ru.mvrlrd.playlistmaker.search.data.network.INTERNET_CONNECTION_ERROR
import ru.mvrlrd.playlistmaker.search.data.network.NO_INTERNET_CONNECTION_CODE
import ru.mvrlrd.playlistmaker.search.data.network.SERVER_ERROR
import ru.mvrlrd.playlistmaker.search.data.network.SUCCESS_CODE
import ru.mvrlrd.playlistmaker.search.data.network.TracksSearchResponse
import ru.mvrlrd.playlistmaker.search.data.storage.LocalStorage
import ru.mvrlrd.playlistmaker.search.domain.Track
import ru.mvrlrd.playlistmaker.search.domain.TracksRepository
import ru.mvrlrd.playlistmaker.search.util.Resource
import ru.mvrlrd.playlistmaker.search.data.model.*

class TracksRepositoryImpl(private val networkClient: NetworkClient, private val localStorage: LocalStorage) :
    TracksRepository {
    override fun searchTracks(query: String): Resource<List<Track>> {
        val response = networkClient.doRequest(
            TracksSearchRequest(
                query
            )
        )
        return when (response.resultCode) {

            NO_INTERNET_CONNECTION_CODE -> {
                println("______${response.resultCode}")
                Resource.Error(message = INTERNET_CONNECTION_ERROR, code = NO_INTERNET_CONNECTION_CODE)
            }
            SUCCESS_CODE -> {
                println("______${response.resultCode}")
                Resource.Success((response as TracksSearchResponse).results.map {
                    it.mapToTrack()
                }, code = SUCCESS_CODE)
            }
            else -> {
                println("______${response.resultCode}")
                Resource.Error(message = SERVER_ERROR, code = response.resultCode)
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
}

