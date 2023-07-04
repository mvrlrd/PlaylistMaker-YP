package ru.mvrlrd.playlistmaker.mediateka.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.search.domain.Track

interface FavoritesRepository {
    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun clearFavorites()
}