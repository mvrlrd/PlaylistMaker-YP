package ru.mvrlrd.playlistmaker.domain

import ru.mvrlrd.playlistmaker.util.Resource

interface TracksRepository {
    fun searchTracks(query: String): Resource<List<Track>>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}