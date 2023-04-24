package ru.mvrlrd.playlistmaker.domain.impl

import ru.mvrlrd.playlistmaker.domain.TracksInteractor
import ru.mvrlrd.playlistmaker.domain.TracksRepository
import ru.mvrlrd.playlistmaker.util.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(query: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(query)){
                is Resource.Success ->{consumer.consume(resource.data, null, resource.code)}
                is Resource.Error -> {consumer.consume(null, resource.message, resource.code)}
            }
        }
    }
}