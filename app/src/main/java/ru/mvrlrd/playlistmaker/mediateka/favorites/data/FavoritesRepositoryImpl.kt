package ru.mvrlrd.playlistmaker.mediateka.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mvrlrd.playlistmaker.database.data.FavoriteDb
import ru.mvrlrd.playlistmaker.database.data.TrackConverter
import ru.mvrlrd.playlistmaker.mediateka.favorites.domain.FavoritesRepository
import ru.mvrlrd.playlistmaker.search.domain.Track

class FavoritesRepositoryImpl(
    private val favoriteDb: FavoriteDb,
    private val trackConverter: TrackConverter
) : FavoritesRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> {
        return flow {
            emit(favoriteDb.getDao().getFavoriteTracks().map {
                trackConverter.mapToAdapter(it)
            })
        }
    }

    override suspend fun clearFavorites() {
            favoriteDb.getDao().clearFavorites()
    }
}