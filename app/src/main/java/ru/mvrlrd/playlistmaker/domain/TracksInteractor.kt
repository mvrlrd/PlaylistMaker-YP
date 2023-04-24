package ru.mvrlrd.playlistmaker.domain

interface TracksInteractor {
    fun searchTracks(query: String, consumer: TracksConsumer)

    interface TracksConsumer{
        fun consume(foundTracks: List<Track>)
    }
}