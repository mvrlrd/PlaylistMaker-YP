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
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.Song
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.relations.PlaylistWithSongs
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.relations.SongWithPlaylists

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(song: Song): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistSongCrossRef(playlistSongCrossRef: PlaylistSongCrossRef): Long


    @Query("SELECT * FROM playlist_table WHERE playlistId =:id")
    suspend fun getPlaylist(id: Long): PlaylistEntity
    @Query("SELECT * FROM song_table")
    fun getSongs(): Flow<List<Song>>
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
    @Query("SELECT * FROM song_table WHERE songId =:songId")
    suspend fun getSongWithPlaylists(songId: Long): SongWithPlaylists


    @Query("SELECT * FROM playlist_song_cross_ref_table")
    suspend fun getAllCrossRef(): List<PlaylistSongCrossRef>

    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)


    @Query("DELETE FROM song_table WHERE songId =:songId")
    suspend fun deleteSong(songId: Long): Int
    @Query("DELETE FROM playlist_table WHERE playlistId =:playlistId")
    suspend fun deletePlaylist(playlistId: Long): Int
    @Query("DELETE FROM playlist_song_cross_ref_table WHERE playlistId =:playlistId AND songId =:trackId")
    suspend fun deleteTrack(trackId: Long, playlistId: Long): Int
    @Query("DELETE FROM playlist_song_cross_ref_table WHERE playlistId =:playlistId")
    suspend fun deleteCrossRef(playlistId: Long): Int

    @Query("DELETE FROM song_table")
    suspend fun clearSongs(): Int
    @Query("DELETE FROM playlist_song_cross_ref_table")
    suspend fun clearCrossReffs(): Int
    @Query("DELETE FROM playlist_table")
    suspend fun clearPlaylists(): Int












}