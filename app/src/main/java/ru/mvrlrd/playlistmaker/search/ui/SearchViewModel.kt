package ru.mvrlrd.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.search.domain.Track
import ru.mvrlrd.playlistmaker.search.domain.TracksInteractor
import ru.mvrlrd.playlistmaker.search.util.Resource

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {
    private val _screenState = MutableLiveData<SearchScreenState>()
    val screenState: LiveData<SearchScreenState> = _screenState

    private var lastQuery: String? = null
    private var latestSearchText: String? = null
    private var searchJob: Job? = null

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    fun onDestroy() {
        searchJob?.cancel()
    }

     fun searchRequest(query: String? = lastQuery) {
        query?.let {
            _screenState.postValue(SearchScreenState.Loading())
            viewModelScope.launch {
                tracksInteractor.searchTracks(query)
                    .collect{
                        resp ->
                            handleResponse(resp.responseCode, resp.data, resp.message)
                    }
            }
        }
    }

    private fun handleResponse(code: Int, tracks: List<Track>?, message: String?){
        if (code==200){
            if (tracks!!.isNotEmpty()){
                _screenState.postValue(SearchScreenState.Success(tracks))
            }else{
                _screenState.postValue(SearchScreenState.NothingFound())
            }
        }else{
            _screenState.postValue(SearchScreenState.Error(message!!, code.toString()))
        }
    }

    fun addToHistory(track: Track) {
        tracksInteractor.addTrackToHistory(track)
    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
        _screenState.postValue(SearchScreenState.ShowHistory(null))
    }

    fun showHistory() {
        if (tracksInteractor.getHistory().isNotEmpty()) {
            _screenState.value = SearchScreenState.ShowHistory(tracksInteractor.getHistory())
        } else {
            _screenState.value = SearchScreenState.Success(null)
        }
    }

    fun isReadyToRender(screenState: SearchScreenState, queryText: String): Boolean {
        if ((screenState is SearchScreenState.Success
                    && queryText.isNotEmpty())
            || (screenState !is SearchScreenState.Success)
        ) {
            return true
        }
        return false
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}