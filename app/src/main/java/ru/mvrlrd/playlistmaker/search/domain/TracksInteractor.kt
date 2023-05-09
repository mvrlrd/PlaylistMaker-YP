package ru.mvrlrd.playlistmaker.search.domain

import ru.mvrlrd.playlistmaker.search.data.Response

interface TracksInteractor {
    fun searchTracks(query: String, consumer: TracksConsumer)
    interface TracksConsumer{
        fun consume(foundTracks: List<Track>?, response: Response)
    }

    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}