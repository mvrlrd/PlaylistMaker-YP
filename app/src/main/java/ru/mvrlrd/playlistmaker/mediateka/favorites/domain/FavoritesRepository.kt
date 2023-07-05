package ru.mvrlrd.playlistmaker.mediateka.favorites.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.search.domain.Track

interface FavoritesRepository {
    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun clearFavorites()
}