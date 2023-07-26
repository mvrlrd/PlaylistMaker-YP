package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistDb
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.Song
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfo
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfoRepository


class PlaylistInfoRepositoryImpl(
    private val database: PlaylistDb,
    private val converter: PlaylistConverter
) : PlaylistInfoRepository {
    override fun getPlaylistWithSongs(id: Long): Flow<PlaylistInfo> {
        return database.getDao().getPlaylistWithSongs(id)
            .map {
                converter.mapPlaylistWithSongsToPlaylistInfo(it)
            }
    }

   override suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long): Flow<Int>{
        val flowable =  database.getDao().deleteTrack(trackId, playlistId)
        val songWithPlaylists = database.getDao().getSongWithPlaylists(trackId)
        if (songWithPlaylists.playlists.isEmpty()){
            database.getDao().deleteSong(trackId)
        }
        return flow { emit(flowable)}
    }

    override fun getAllSongsForDebug(): Flow<List<Song>> {
        return database.getDao().getSongs()
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        val playlistWithSongs = database.getDao().getPlaylistWithSongsSuspend(playlistId)
        CoroutineScope(Dispatchers.IO).launch{
            val countOfDeletedTracks = deleteSongsOfPlaylist(playlistWithSongs.songs)
            val countOfDeletedCrossRefs = database.getDao().deleteCrossRef(playlistId)
            val countOfDeletedPlaylists = database.getDao().deletePlaylist(playlistId)
            tag("deleted:  playlist = $countOfDeletedPlaylists, songs = $countOfDeletedTracks, crossRef = $countOfDeletedCrossRefs;")
        }
    }

    private suspend fun deleteSongsOfPlaylist(songs: List<Song>): Int {
        var countOfDeletedTracks = 0
        songs.forEach {
            val size = database.getDao().getSongWithPlaylists(it.songId).playlists.size
            if (size == 1) {
                countOfDeletedTracks += database.getDao().deleteSong(it.songId)
            }
        }
        return countOfDeletedTracks
    }

    private suspend fun clearAll(){
        val songs = database.getDao().clearSongs()
        val playlists = database.getDao().clearPlaylists()
        val crossRef = database.getDao().clearCrossReffs()
        tag("deleted:  playlists = $playlists,  songs = $songs,  crossRef = $crossRef;")
    }

    private fun tag(text: String){
        Log.e(TAG, text)
    }
    companion object{
        private const val TAG = "PlaylistInfoRepositoryImpl"
    }
}