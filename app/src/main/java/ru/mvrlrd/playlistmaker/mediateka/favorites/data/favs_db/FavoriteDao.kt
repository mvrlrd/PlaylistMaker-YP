package ru.mvrlrd.playlistmaker.mediateka.favorites.data.favs_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)
    @Query("DELETE FROM favorite_tracks WHERE id =:id")
    suspend fun deleteTrack(id: Long)
    @Query("SELECT * FROM favorite_tracks ORDER by date DESC")
    suspend fun getFavoriteTracks(): List<TrackEntity>
    @Query("SELECT id FROM favorite_tracks")
    suspend fun getFavoriteTrackIds(): List<Long>
    @Query("DELETE FROM favorite_tracks")
    suspend fun clearFavorites()

    @Query("SELECT id FROM favorite_tracks")
    fun getFavIds(): List<Long>


    @Query("SELECT id FROM favorite_tracks")
    fun getFavIds2(): Flow<List<Long>>


}