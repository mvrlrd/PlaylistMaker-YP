package ru.mvrlrd.playlistmaker.mediateka.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.favorites.domain.FavoriteInteractor
import ru.mvrlrd.playlistmaker.search.domain.Track

class FavoritesViewModel(private val favoriteInteractor: FavoriteInteractor) : ViewModel() {
    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val _screenState = MutableLiveData<FavoriteScreenState>()
    val screenState : LiveData<FavoriteScreenState> get() = _screenState

    private var loadingFavoritesJob: Job? = null
    init {
       updateFavorites()
    }
    fun loading(){
        _screenState.value = (FavoriteScreenState.Loading())
    }
    fun updateFavorites(){
        loading()
        loadingFavoritesJob?.cancel()
        loadingFavoritesJob = viewModelScope.launch {
            favoriteInteractor.getFavoriteTracks().collect() { favorites ->
                _tracks.postValue(favorites)
                delay(PROGRESS_BAR_DURATION)
                if (favorites.isEmpty()){
                    _screenState.postValue(FavoriteScreenState.Empty())
                }else{
                    _screenState.postValue(FavoriteScreenState.Loaded())
                }
            }
        }
    }

    fun onDestroy(){
        loadingFavoritesJob?.cancel()
    }
    companion object{
        private const val PROGRESS_BAR_DURATION = 300L
    }
}