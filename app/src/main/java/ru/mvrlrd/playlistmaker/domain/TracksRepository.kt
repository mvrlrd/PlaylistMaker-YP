package ru.mvrlrd.playlistmaker.domain

import ru.mvrlrd.playlistmaker.util.Resource

interface TracksRepository {
    fun searchTracks(query: String): Resource<List<Track>>
}