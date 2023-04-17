package ru.mvrlrd.playlistmaker.presenter

import androidx.lifecycle.LiveData
import ru.mvrlrd.playlistmaker.data.model.TrackInteractorImpl
import ru.mvrlrd.playlistmaker.data.network.NetworkHandler
import ru.mvrlrd.playlistmaker.domain.AddToHistoryUseCase
import ru.mvrlrd.playlistmaker.domain.GetTrackListUseCase
import ru.mvrlrd.playlistmaker.domain.SearchUseCase
import ru.mvrlrd.playlistmaker.domain.Track



class SearchPresenter {
private val trackInteractor = TrackInteractorImpl(NetworkHandler())
    private val searchUseCase : SearchUseCase = SearchUseCase(trackInteractor)
    private val addToHistoryUseCase: AddToHistoryUseCase = AddToHistoryUseCase(trackInteractor)
    private val getTrackListUseCase: GetTrackListUseCase = GetTrackListUseCase(trackInteractor)

    val tracks: LiveData<List<Track>>
        get() = getTrackListUseCase.getTrackList()

    fun searchTrack(query: String){
        searchUseCase.search(query)
    }

    fun addTrackToHistory(track: Track){
        addToHistoryUseCase.addToHistory(track)
    }
}