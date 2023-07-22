package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_db.entities.PlaylistEntity
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_db.entities.PlaylistSongCrossRef
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_db.entities.Song
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_db.relations.PlaylistWithSongs

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table WHERE playlistId =:id")
    suspend fun getPlaylist(id: Long): PlaylistEntity

    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(song: Song): Long

    @Transaction
    @Query("SELECT * FROM playlist_table")
    fun getPlaylistsWithSongs(): Flow<List<PlaylistWithSongs>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistSongCrossRef(playlistSongCrossRef: PlaylistSongCrossRef): Long
}