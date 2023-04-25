package ru.mvrlrd.playlistmaker.ui.search

import android.app.Application
import android.os.Handler
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

    private val _toast = MutableLiveData<PlaceHolderState>()
    val toast: LiveData<PlaceHolderState> = _toast

    private val _toastState = MutableLiveData<String>()
    val toastState: LiveData<String> = _toastState
//    val handler = Handler(application.mainLooper)
    // Код без изменений

    fun showToast(message: String) {
        _toastState.postValue(message)
    }

     fun searchRequest(query: String){
         _toast.postValue(PlaceHolderState.Loading())
        tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer{
            override fun consume(foundTracks: List<Track>?, errorMessage: String?, code: Int) {
                when (code){
                    200 -> {
                        if (foundTracks!!.isNotEmpty()) {
                            _toast.postValue(PlaceHolderState.Empty())
                            _tracksLiveData.postValue(foundTracks)
                        }else{
                            _toast.postValue(PlaceHolderState.NothingFound())
//                            _toastState.postValue("nothing")
                        }

                    }
                    else ->{
                        _toast.postValue(PlaceHolderState.Error(errorMessage))
//                        _toastState.postValue(errorMessage)
                        Log.e("SearchViewModel", errorMessage?:"error code: $code")
                    }
                }
            }
        })
    }

    fun clearTrackList(){
        _tracksLiveData.value = emptyList()
    }

    fun addToHistory(track: Track){
        tracksInteractor.addTrackToHistory(track)
    }
    fun clearHistory(){
        tracksInteractor.clearHistory()
    }
    fun getHistory(): List<Track>{
        return tracksInteractor.getHistory()
    }

}