package ru.mvrlrd.playlistmaker.playlistDb.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.playlistDb.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

//    @Transaction
//    @Query("SELECT * FROM playlist_table")
//    fun getPlaylistWithTracks(): List<PlaylistWithTracks>

    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>


}