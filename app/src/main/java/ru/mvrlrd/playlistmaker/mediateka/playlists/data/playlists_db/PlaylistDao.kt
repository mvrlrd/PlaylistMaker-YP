package ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.PlaylistEntity
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.PlaylistSongCrossRef
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.TrackEntity
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.relations.PlaylistWithSongs
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.relations.SongWithPlaylists

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(trackEntity: TrackEntity): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistSongCrossRef(playlistSongCrossRef: PlaylistSongCrossRef): Long


    @Query("SELECT * FROM playlist_table WHERE playlistId =:id")
    suspend fun getPlaylist(id: Long): PlaylistEntity
    @Query("SELECT * FROM track_table")
    fun getSongs(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM track_table WHERE trackId =:id")
    suspend fun getSong(id: Long): TrackEntity
    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
    @Transaction
    @Query("SELECT * FROM playlist_table")
    fun getPlaylistsWithSongs(): Flow<List<PlaylistWithSongs>>
    @Transaction
    @Query("SELECT * FROM playlist_table WHERE playlistId =:id")
    fun getPlaylistWithSongs(id: Long): Flow<PlaylistWithSongs>

    @Transaction
    @Query("SELECT * FROM playlist_table WHERE playlistId =:id")
   suspend fun getPlaylistWithSongsSuspend(id: Long): PlaylistWithSongs
    @Transaction
    @Query("SELECT * FROM track_table WHERE trackId =:trackId")
    suspend fun getSongWithPlaylists(trackId: Long): SongWithPlaylists


    @Query("SELECT * FROM playlist_song_cross_ref_table")
    suspend fun getAllCrossRef(): List<PlaylistSongCrossRef>
    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)


    @Query("DELETE FROM track_table WHERE trackId =:trackId")
    suspend fun deleteSong(trackId: Long): Int
    @Query("DELETE FROM playlist_table WHERE playlistId =:playlistId")
    suspend fun deletePlaylist(playlistId: Long): Int
    @Query("DELETE FROM playlist_song_cross_ref_table WHERE playlistId =:playlistId AND trackId =:trackId")
    suspend fun deleteTrack(trackId: Long, playlistId: Long): Int
    @Query("DELETE FROM playlist_song_cross_ref_table WHERE playlistId =:playlistId")
    suspend fun deleteCrossRef(playlistId: Long): Int
    @Query("DELETE FROM playlist_song_cross_ref_table WHERE playlistId =:playlistId AND trackId =:trackId")
    suspend fun deleteCrossRef(playlistId: Long, trackId:Long): Int

    @Query("DELETE FROM track_table")
    suspend fun clearSongs(): Int
    @Query("DELETE FROM playlist_song_cross_ref_table")
    suspend fun clearCrossReffs(): Int
    @Query("DELETE FROM playlist_table")
    suspend fun clearPlaylists(): Int





    @Transaction
    @Query("SELECT * FROM playlist_song_cross_ref_table WHERE playlistId =:id ORDER by date DESC")
    suspend fun getCrossRefByDesc(id: Long): List<PlaylistSongCrossRef>

    @Transaction
    @Query("SELECT * FROM track_table WHERE trackId IN (:ids)")
    suspend fun getSongsByDesc(ids: List<Long>): List<TrackEntity>



}