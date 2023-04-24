package ru.mvrlrd.playlistmaker.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.mvrlrd.playlistmaker.Creator
import ru.mvrlrd.playlistmaker.domain.Track
import ru.mvrlrd.playlistmaker.domain.TracksInteractor

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val tracksInteractor = Creator.provideTracksInteractor(getApplication<Application>())
    private val _tracksLiveData = MutableLiveData<List<Track>>()
    val tracksLiveData: LiveData<List<Track>> = _tracksLiveData

     fun searchRequest(query: String){
        tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer{
            override fun consume(foundTracks: List<Track>?, errorMessage: String?, code: Int) {
                when (code){
                    200 -> {
                        _tracksLiveData.postValue(foundTracks)
                    }
                    else ->{
                        Log.e("SearchViewModel", errorMessage?:"error code: $code")
                    }
                }
            }
        })
    }

}