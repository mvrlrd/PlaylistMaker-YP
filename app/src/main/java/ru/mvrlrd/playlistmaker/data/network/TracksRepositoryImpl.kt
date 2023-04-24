package ru.mvrlrd.playlistmaker.data.network

import ru.mvrlrd.playlistmaker.data.NetworkClient
import ru.mvrlrd.playlistmaker.data.TracksSearchRequest
import ru.mvrlrd.playlistmaker.data.model.mapToTrack
import ru.mvrlrd.playlistmaker.domain.Track
import ru.mvrlrd.playlistmaker.domain.TracksRepository

class TracksRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {
    override fun searchTracks(query: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(query))
        if (response.resultCode == SUCCESS_CODE){
            return (response as TracksSearchResponse).results.map {
                it.mapToTrack()
            }
        }else{
            return emptyList()
        }
    }
}

