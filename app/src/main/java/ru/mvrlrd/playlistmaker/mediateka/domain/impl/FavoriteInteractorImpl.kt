package ru.mvrlrd.playlistmaker.mediateka.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.domain.FavoriteInteractor
import ru.mvrlrd.playlistmaker.mediateka.domain.FavoritesRepository
import ru.mvrlrd.playlistmaker.search.domain.Track

class FavoriteInteractorImpl(val favoriteRepository: FavoritesRepository) : FavoriteInteractor {
    override suspend fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteRepository.getFavoriteTracks()
    }

    override suspend fun clearFavorites() {
        favoriteRepository.clearFavorites()
    }
}