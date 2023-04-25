package ru.mvrlrd.playlistmaker.data

import ru.mvrlrd.playlistmaker.data.model.mapToTrack
import ru.mvrlrd.playlistmaker.data.network.INTERNET_CONNECTION_ERROR
import ru.mvrlrd.playlistmaker.data.network.NO_INTERNET_CONNECTION_CODE
import ru.mvrlrd.playlistmaker.data.network.SERVER_ERROR
import ru.mvrlrd.playlistmaker.data.network.SUCCESS_CODE
import ru.mvrlrd.playlistmaker.data.network.TracksSearchResponse
import ru.mvrlrd.playlistmaker.data.storage.LocalStorage
import ru.mvrlrd.playlistmaker.domain.Track
import ru.mvrlrd.playlistmaker.domain.TracksRepository
import ru.mvrlrd.playlistmaker.util.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient, private val localStorage: LocalStorage) : TracksRepository {
    override fun searchTracks(query: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(query))
        return when (response.resultCode) {
            NO_INTERNET_CONNECTION_CODE -> {
                Resource.Error(message = INTERNET_CONNECTION_ERROR, code = NO_INTERNET_CONNECTION_CODE)
            }
            SUCCESS_CODE -> {
                Resource.Success((response as TracksSearchResponse).results.map {
                    it.mapToTrack()
                }, code = SUCCESS_CODE)
            }
            else -> {
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

