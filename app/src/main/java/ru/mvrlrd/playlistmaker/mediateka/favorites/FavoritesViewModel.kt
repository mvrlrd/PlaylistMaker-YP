package ru.mvrlrd.playlistmaker.mediateka.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.domain.FavoriteInteractor
import ru.mvrlrd.playlistmaker.search.domain.Track

class FavoritesViewModel(private val favoriteInteractor: FavoriteInteractor) : ViewModel() {
    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    init {
       updateFavorites()
    }

    fun updateFavorites(){

        viewModelScope.launch {
//            favoriteInteractor.clearFavorites()
            favoriteInteractor.getFavoriteTracks().collect() { favorites ->
                _tracks.postValue(favorites)
            }
        }
    }
}