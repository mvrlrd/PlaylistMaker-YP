package ru.mvrlrd.playlistmaker.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.mvrlrd.playlistmaker.Creator
import ru.mvrlrd.playlistmaker.domain.Track
import ru.mvrlrd.playlistmaker.domain.TracksInteractor

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val tracksInteractor = Creator.provideTracksInteractor(getApplication<Application>())

    private val _screenState = MutableLiveData<SearchScreenState>()
    val screenState: LiveData<SearchScreenState> = _screenState

//    val handler = Handler(application.mainLooper)
    // Код без изменений

     fun searchRequest(query: String){
         _screenState.postValue(SearchScreenState.Loading())
        tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer{
            override fun consume(foundTracks: List<Track>?, errorMessage: String?, code: Int) {
                when (code){
                    200 -> {
                        if (foundTracks!!.isNotEmpty()) {
                            _screenState.postValue(SearchScreenState.Success(foundTracks))
                        }else{
                            _screenState.postValue(SearchScreenState.NothingFound())
                        }
                    }
                    else ->{
                        _screenState.postValue(SearchScreenState.Error(errorMessage))
                    }
                }
            }
        })
    }

    fun addToHistory(track: Track){
        tracksInteractor.addTrackToHistory(track)
    }
    fun clearHistory(){
        tracksInteractor.clearHistory()
        _screenState.postValue(SearchScreenState.Success(null))
    }
    fun showHistory(){
        if (tracksInteractor.getHistory().isNotEmpty()){
            _screenState.value = SearchScreenState.ShowHistory(tracksInteractor.getHistory())
        }else{
            _screenState.value = SearchScreenState.Success(null)
        }

    }

}