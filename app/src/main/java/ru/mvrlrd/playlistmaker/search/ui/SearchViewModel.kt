package ru.mvrlrd.playlistmaker.search.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter
import ru.mvrlrd.playlistmaker.search.domain.TracksInteractor

class SearchViewModel(private val tracksInteractor: TracksInteractor,
                      private val context: Application) : AndroidViewModel(context) {
    private val _screenState = MutableLiveData<SearchScreenState>()
    val screenState: LiveData<SearchScreenState> = _screenState

    private var lastQuery: String? = null
    private var latestSearchText: String? = null
    private var searchJob: Job? = null
    private var updateFavsJob : Job? = null

    private val favIds = mutableListOf<Long>()

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
        updateFavsJob?.cancel()
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
                        handleResponse(trackForAdapters = trackList, code = code, errorMessage = errorMessage)
                    }
            }
        }
    }

    fun updateFavIds(){
       updateFavsJob = viewModelScope.launch {
            tracksInteractor.getFavIds().collect(){
                favIds.clear()
                favIds.addAll(it)
            }
        }
    }

    fun isFavorite(trackId: Long) :Boolean {
       return favIds.contains(trackId)
    }

    private fun handleResponse(trackForAdapters: List<TrackForAdapter>?, code: Int, errorMessage: String?){
        if (code == context.resources.getString(R.string.success_code).toInt()){
            if (trackForAdapters!!.isNotEmpty()){
                _screenState.postValue(SearchScreenState.Success(trackForAdapters))
            }else{
                _screenState.postValue(SearchScreenState.NothingFound())
            }
        }else{
            _screenState.postValue(SearchScreenState.Error(code.toString(), errorMessage!!))
        }
    }

    fun addToHistory(trackForAdapter: TrackForAdapter) {
        tracksInteractor.addTrackToHistory(trackForAdapter)
    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
        _screenState.postValue(SearchScreenState.ShowHistory(null))
    }

    fun showHistory() {
        viewModelScope.launch {
            tracksInteractor.getHistory().collect() { historyTracks ->
                if (historyTracks.isNotEmpty()) {
                    _screenState.value = SearchScreenState.ShowHistory(historyTracks)
                } else {
                    _screenState.value = SearchScreenState.ShowHistory(null)
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