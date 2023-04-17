package ru.mvrlrd.playlistmaker.domain

import androidx.lifecycle.LiveData

interface TrackInteractor {
    fun search(query: String)
    fun addToHistory(track:Track)
    fun getTrackList(): LiveData<List<Track>>

}