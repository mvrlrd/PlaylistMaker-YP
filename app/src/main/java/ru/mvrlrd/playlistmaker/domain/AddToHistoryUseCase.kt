package ru.mvrlrd.playlistmaker.domain

class AddToHistoryUseCase(private val trackInteractor: TrackInteractor) {
    fun addToHistory(track: Track){
        trackInteractor.addToHistory(track)
    }
}