package ru.mvrlrd.playlistmaker.domain

interface TracksRepository {
    fun searchTracks(query: String): List<Track>
}