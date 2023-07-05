package ru.mvrlrd.playlistmaker.mediateka.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.favorites.domain.FavoriteInteractor
import ru.mvrlrd.playlistmaker.mediateka.favorites.domain.FavoritesRepository
import ru.mvrlrd.playlistmaker.search.domain.Track

class FavoriteInteractorImpl(val favoriteRepository: FavoritesRepository) : FavoriteInteractor {
    override suspend fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteRepository.getFavoriteTracks()
    }

    override suspend fun clearFavorites() {
        favoriteRepository.clearFavorites()
    }
}