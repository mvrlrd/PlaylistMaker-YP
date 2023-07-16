package ru.mvrlrd.playlistmaker.db_playlist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.db_playlist.PlaylistEntity
import ru.mvrlrd.playlistmaker.db_playlist.data.entities.PlaylistSongCrossRef
import ru.mvrlrd.playlistmaker.db_playlist.data.entities.Song
import ru.mvrlrd.playlistmaker.db_playlist.data.relations.PlaylistWithSongs

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(song: Song)

    @Transaction
    @Query("SELECT * FROM playlist_table")
     fun getPlaylistsWithSongs(): Flow<List<PlaylistWithSongs>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistSongCrossRef(playlistSongCrossRef: PlaylistSongCrossRef)
}