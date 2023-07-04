package ru.mvrlrd.playlistmaker.search.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.search.domain.Track
import ru.mvrlrd.playlistmaker.search.domain.TracksInteractor

class SearchViewModel(private val tracksInteractor: TracksInteractor, private val context: Application) : AndroidViewModel(context) {
    private val _screenState = MutableLiveData<SearchScreenState>()
    val screenState: LiveData<SearchScreenState> = _screenState

    private var lastQuery: String? = null
    private var latestSearchText: String? = null
    private var searchJob: Job? = null

    fun searchDebounce(changedText: String) {
        if (changedText.isNotEmpty()) {
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
    }

    fun onDestroy() {
        searchJob?.cancel()
    }

    fun searchRequest(query: String? = lastQuery) {
        query?.let {
            _screenState.postValue(SearchScreenState.Loading())
            searchJob?.cancel()
            viewModelScope.launch {
                tracksInteractor.searchTracks(query)
                    .collect{
                        resp ->
                        val trackList = resp.first
                        val code = resp.second.first
                        val errorMessage = resp.second.second
                        handleResponse(tracks = trackList, code = code, errorMessage = errorMessage)
                    }
            }
        }
    }

    private fun handleResponse(tracks: List<Track>?, code: Int, errorMessage: String?){
        if (code == context.resources.getString(R.string.success_code).toInt()){
            if (tracks!!.isNotEmpty()){
                _screenState.postValue(SearchScreenState.Success(tracks))
            }else{
                _screenState.postValue(SearchScreenState.NothingFound())
            }
        }else{
            _screenState.postValue(SearchScreenState.Error(code.toString(), errorMessage!!))
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
        viewModelScope.launch {
            tracksInteractor.getHistory().collect() { historyTracks ->
                if (historyTracks.isNotEmpty()) {
                    historyTracks.forEach {
                        println(it.isFavorite)
                    }
                    _screenState.value = SearchScreenState.ShowHistory(historyTracks)
                } else {
                    _screenState.value = SearchScreenState.Success(null)
                }
            }
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