package ru.mvrlrd.playlistmaker.domain

import androidx.lifecycle.LiveData

class GetTrackListUseCase(private val trackInteractor: TrackInteractor) {
    fun getTrackList(): LiveData<List<Track>>{
        return trackInteractor.getTrackList()
    }
}