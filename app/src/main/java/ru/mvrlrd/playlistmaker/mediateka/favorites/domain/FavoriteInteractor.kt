package ru.mvrlrd.playlistmaker.mediateka.favorites.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.search.domain.AdapterTrack

interface FavoriteInteractor {
    suspend fun getFavoriteTracks(): Flow<List<AdapterTrack>>
    suspend fun clearFavorites()
}