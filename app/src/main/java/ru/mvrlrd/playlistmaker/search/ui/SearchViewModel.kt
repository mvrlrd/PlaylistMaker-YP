package ru.mvrlrd.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mvrlrd.playlistmaker.search.data.Response
import ru.mvrlrd.playlistmaker.search.data.TracksRepositoryImpl.Companion.SUCCESS_CODE
import ru.mvrlrd.playlistmaker.search.domain.Track
import ru.mvrlrd.playlistmaker.search.domain.TracksInteractor

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {
    private val _screenState = MutableLiveData<SearchScreenState>()
    val screenState: LiveData<SearchScreenState> = _screenState
    private val handler = Handler(Looper.getMainLooper())
    private var lastQuery: String? = null
    private var isClickAllowed = true

    private fun makeDelaySearching(changedText: String) {
        val searchRunnable = Runnable { searchRightAway(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun trackOnClickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
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

    fun searchRightAway(query: String? = lastQuery) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        query?.let {
            _screenState.postValue(SearchScreenState.Loading())
            tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, responseCode: Int, message: String?) {
                    when (responseCode) {
                        SUCCESS_CODE -> {
                            if (foundTracks!!.isNotEmpty()) {
                                _screenState.postValue(SearchScreenState.Success(foundTracks))
                            } else {
                                _screenState.postValue(SearchScreenState.NothingFound())
                            }
                        }
                        else -> {
                            _screenState.postValue(SearchScreenState.Error(message, responseCode.toString()))
                        }
                    }
                }
            })
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
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val CLICK_DEBOUNCE_DELAY = 1000L

    }
}