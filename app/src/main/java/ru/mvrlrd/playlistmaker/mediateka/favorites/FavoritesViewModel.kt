package ru.mvrlrd.playlistmaker.mediateka.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mvrlrd.playlistmaker.mediateka.favorites.domain.FavoriteInteractor

class FavoritesViewModel(interactor: FavoriteInteractor) : ViewModel() {
    val trackList = interactor.getFavoriteTracks()
    private val _screenState = MutableLiveData<FavoriteScreenState>()
    val screenState: LiveData<FavoriteScreenState> get() = _screenState

    fun emptyHistory() {
        _screenState.postValue(FavoriteScreenState.Empty)
    }

    fun loadHistory() {
        _screenState.postValue(FavoriteScreenState.Loaded)
    }
}