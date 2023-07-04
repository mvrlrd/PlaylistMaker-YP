package ru.mvrlrd.playlistmaker.favorites.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)
    @Query("DELETE FROM favorite_tracks WHERE id =:id")
    suspend fun deleteTrack(id: Int)
    @Query("SELECT * FROM favorite_tracks")
    suspend fun getFavoriteTracks(): List<TrackEntity>
    @Query("SELECT id FROM favorite_tracks")
    suspend fun getFavoriteTrackIds(): List<Int>
}