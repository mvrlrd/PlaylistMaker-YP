package ru.mvrlrd.playlistmaker.domain.impl

import ru.mvrlrd.playlistmaker.domain.TracksInteractor
import ru.mvrlrd.playlistmaker.domain.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(query: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(query))
        }
    }
}