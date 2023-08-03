package ru.mvrlrd.playlistmaker.mediateka.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.favorites.domain.FavoriteInteractor
import ru.mvrlrd.playlistmaker.mediateka.favorites.domain.FavoritesRepository
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

class FavoriteInteractorImpl(val favoriteRepository: FavoritesRepository) : FavoriteInteractor {
    override fun getFavoriteTracks(): Flow<List<TrackForAdapter>> {
        return favoriteRepository.getFavoriteTracks()
    }
}