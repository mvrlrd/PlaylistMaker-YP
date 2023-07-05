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

    private val _screenState = MutableLiveData<FavoriteScreenState>()
    val screenState : LiveData<FavoriteScreenState> get() = _screenState

    init {
       updateFavorites()
    }

    fun updateFavorites(){
        _screenState.value = (FavoriteScreenState.Loading())
        viewModelScope.launch {
            favoriteInteractor.getFavoriteTracks().collect() { favorites ->
                _tracks.postValue(favorites)
                if (favorites.isEmpty()){
                    _screenState.postValue(FavoriteScreenState.Empty())
                }else{
                    _screenState.postValue(FavoriteScreenState.Loaded())
                }
            }
        }
    }
}