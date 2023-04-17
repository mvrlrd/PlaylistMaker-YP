package ru.mvrlrd.playlistmaker.domain

class SearchUseCase(private val trackInteractor: TrackInteractor) {
    fun search(query: String){
        trackInteractor.search(query)
    }
}