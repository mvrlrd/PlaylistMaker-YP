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
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.TrackEntity
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfo
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfoRepository
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter


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

    override suspend fun getTrackListByDescDate(playlistId: Long): List<TrackForAdapter>{
        val  crossRef = database.getDao().getCrossRefByDesc(playlistId)
        val songsByDescDate = mutableListOf<TrackEntity>()
        crossRef.forEach {
            songsByDescDate.add(database.getDao().getSong(it.trackId))
        }
        return  converter.mapEntitiesToTracks(songsByDescDate)
    }


   override suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long): Flow<Int> {
       val flowable = database.getDao().deleteTrack(trackId, playlistId)
       val songWithPlaylists = database.getDao().getSongWithPlaylists(trackId)
       database.getDao().deleteCrossRef(playlistId = playlistId, trackId = trackId)
       if (songWithPlaylists.playlists.isEmpty()) {
           database.getDao().deleteSong(trackId)
       }
       return flow { emit(flowable) }
   }

    override fun getAllSongsForDebug(): Flow<List<TrackEntity>> {
        return database.getDao().getSongs()
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        val playlistWithSongs = database.getDao().getPlaylistWithSongsSuspend(playlistId)
        CoroutineScope(Dispatchers.IO).launch{
            val countOfDeletedTracks = deleteSongsOfPlaylist(playlistWithSongs.trackEntities)
            val countOfDeletedCrossRefs = database.getDao().deleteCrossRef(playlistId)
            val countOfDeletedPlaylists = database.getDao().deletePlaylist(playlistId)
            tag("deleted:  playlist = $countOfDeletedPlaylists, songs = $countOfDeletedTracks, crossRef = $countOfDeletedCrossRefs;")
        }
    }

    private suspend fun deleteSongsOfPlaylist(trackEntities: List<TrackEntity>): Int {
        var countOfDeletedTracks = 0
        trackEntities.forEach {
            val size = database.getDao().getSongWithPlaylists(it.trackId).playlists.size
            if (size == MIN_COUNT_OF_PLAYLISTS_WHERE_TRACK_CAN_BE) {
                countOfDeletedTracks += database.getDao().deleteSong(it.trackId)
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
        private const val MIN_COUNT_OF_PLAYLISTS_WHERE_TRACK_CAN_BE = 1
        private const val TAG = "PlaylistInfoRepositoryImpl"
    }
}