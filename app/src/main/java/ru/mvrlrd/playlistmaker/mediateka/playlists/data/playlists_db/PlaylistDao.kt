package ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.PlaylistEntity
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.PlaylistTrackCrossRef
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.TrackEntity
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.relations.PlaylistWithTracks
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.relations.TrackWithPlaylists

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(trackEntity: TrackEntity): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrackCrossRef(playlistTrackCrossRef: PlaylistTrackCrossRef): Long


    @Query("SELECT * FROM playlist_table WHERE playlistId =:id")
    suspend fun getPlaylist(id: Long): PlaylistEntity
    @Query("SELECT * FROM track_table")
    fun getTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM track_table WHERE trackId =:id")
    suspend fun getTrack(id: Long): TrackEntity
    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
    @Transaction
    @Query("SELECT * FROM playlist_table")
    fun getPlaylistsWithTracks(): Flow<List<PlaylistWithTracks>>
    @Transaction
    @Query("SELECT * FROM playlist_table WHERE playlistId =:id")
    fun getPlaylistWithTracks(id: Long): Flow<PlaylistWithTracks>

    @Transaction
    @Query("SELECT * FROM playlist_table WHERE playlistId =:id")
   suspend fun getPlaylistWithTracksSuspend(id: Long): PlaylistWithTracks
    @Transaction
    @Query("SELECT * FROM track_table WHERE trackId =:trackId")
    suspend fun getTrackWithPlaylists(trackId: Long): TrackWithPlaylists


    @Query("SELECT * FROM playlist_song_cross_ref_table")
    suspend fun getAllCrossRef(): List<PlaylistTrackCrossRef>
    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)




    @Query("UPDATE playlist_table SET name = :newName WHERE playlistId =:id")
    suspend fun updatePlaylistName(id: Long, newName: String)




    @Query("DELETE FROM track_table WHERE trackId =:trackId")
    suspend fun deleteTrack(trackId: Long): Int
    @Query("DELETE FROM playlist_table WHERE playlistId =:playlistId")
    suspend fun deletePlaylist(playlistId: Long): Int
    @Query("DELETE FROM playlist_song_cross_ref_table WHERE playlistId =:playlistId AND trackId =:trackId")
    suspend fun deleteTrackPlaylistCrossRef(trackId: Long, playlistId: Long): Int
    @Query("DELETE FROM playlist_song_cross_ref_table WHERE playlistId =:playlistId")
    suspend fun deleteCrossRef(playlistId: Long): Int
//    @Query("DELETE FROM playlist_song_cross_ref_table WHERE playlistId =:playlistId AND trackId =:trackId")
//    suspend fun deleteCrossRef(playlistId: Long, trackId:Long): Int

    @Query("DELETE FROM track_table")
    suspend fun clearTracks(): Int
    @Query("DELETE FROM playlist_song_cross_ref_table")
    suspend fun clearCrossRefs(): Int
    @Query("DELETE FROM playlist_table")
    suspend fun clearPlaylists(): Int





    @Transaction
    @Query("SELECT * FROM playlist_song_cross_ref_table WHERE playlistId =:id ORDER by date DESC")
    suspend fun getCrossRefByDesc(id: Long): List<PlaylistTrackCrossRef>

    @Transaction
    @Query("SELECT * FROM track_table WHERE trackId IN (:ids)")
    suspend fun getSongsByDesc(ids: List<Long>): List<TrackEntity>



}