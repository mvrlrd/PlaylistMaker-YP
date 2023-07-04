package ru.mvrlrd.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.mvrlrd.playlistmaker.search.data.ILocalStorage
import ru.mvrlrd.playlistmaker.search.data.model.*
import ru.mvrlrd.playlistmaker.search.domain.Track

class LocalStorage(private val sharedPreferences: SharedPreferences) : ILocalStorage {
    private companion object {
        const val HISTORY_KEY = "HISTORY_KEY"
        const val MAX_SIZE_OF_HISTORY_LIST = 10
    }

   override fun addToHistory(track: Track) {
        val searchedTracks = getHistory().map { it.mapToTrackToStorage() } as MutableList

        if (searchedTracks.contains(track.mapToTrackToStorage())){
            searchedTracks.remove(track)
        }
        searchedTracks.add(0,track)
        if (searchedTracks.size> MAX_SIZE_OF_HISTORY_LIST){
            searchedTracks.removeLast()
        }
        val json = Gson().toJson(searchedTracks)
        sharedPreferences
            .edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

    override fun clearHistory() {
        sharedPreferences
            .edit()
            .clear()
            .apply()
    }

    override fun getHistory(): List<Track>{
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return arrayListOf()
        val tracksToStorage = Gson().fromJson(json, Array<TrackToStorage>::class.java).toCollection(ArrayList())
        return tracksToStorage.map { it.mapToTrack() }
    }
}