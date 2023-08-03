package ru.mvrlrd.playlistmaker.mediateka.favorites.domain

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

interface FavoritesRepository {
    fun getFavoriteTracks(): Flow<List<TrackForAdapter>>
}