package ru.mvrlrd.playlistmaker.mediateka.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mvrlrd.playlistmaker.mediateka.favorites.data.favs_db.FavoriteDb
import ru.mvrlrd.playlistmaker.mediateka.favorites.data.favs_db.TrackConverter
import ru.mvrlrd.playlistmaker.mediateka.favorites.domain.FavoritesRepository
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

class FavoritesRepositoryImpl(
    private val favoriteDb: FavoriteDb,
    private val trackConverter: TrackConverter
) : FavoritesRepository {
    override fun getFavoriteTracks(): Flow<List<TrackForAdapter>> {
        return flow {
            emit(favoriteDb.getDao().getFavoriteTracks().map {
                trackConverter.mapEntityToAdapterTrack(it)
            })
        }
    }

    override suspend fun clearFavorites() {
        favoriteDb.getDao().clearFavorites()
    }
}