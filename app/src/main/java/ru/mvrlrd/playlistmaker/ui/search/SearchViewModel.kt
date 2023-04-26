package ru.mvrlrd.playlistmaker.ui.search

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
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


    private val handler = Handler(Looper.getMainLooper())

    private var lastQuery: String? = null

     fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private fun makeDelaySearching(changedText: String){
        val searchRunnable = Runnable { searchRequest(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }
    fun searchDebounce(changedText: String? = lastQuery) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        if (!changedText.isNullOrEmpty()) {
            if ((lastQuery == changedText)) {
                return
            }
            this.lastQuery = changedText
            makeDelaySearching(changedText)
        }
    }

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

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

}