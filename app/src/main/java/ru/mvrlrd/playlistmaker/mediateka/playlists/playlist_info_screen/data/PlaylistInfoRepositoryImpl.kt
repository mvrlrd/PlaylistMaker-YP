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
    override fun getPlaylistInfo(id: Long): Flow<PlaylistInfo> {
        return database.getDao().getPlaylistWithTracks(id)
            .map {
                converter.mapPlaylistWithSongsToPlaylistInfo(it)
            }
    }

    override suspend fun getTracksByDescDate(playlistId: Long): List<TrackForAdapter>{
        val  crossRef = database.getDao().getCrossRefByDesc(playlistId)
        val songsByDescDate = mutableListOf<TrackEntity>()
        crossRef.forEach {
            songsByDescDate.add(database.getDao().getTrack(it.trackId))
        }
        return  converter.mapEntitiesToTracks(songsByDescDate)
    }


   override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long): Flow<Int> {
       val flowable = database.getDao().deleteTrackPlaylistCrossRef(trackId, playlistId)
       checkIfTrackIsInAnotherPlaylists(trackId)
       return flow { emit(flowable) }
   }

    private suspend fun checkIfTrackIsInAnotherPlaylists(trackId: Long) {
        val trackWithPlaylists = database.getDao().getTrackWithPlaylists(trackId)
        Log.e("PlaylistInfoRepositoryImpl", "track remains in ${trackWithPlaylists.playlists.size} of playlists")
        if (trackWithPlaylists.playlists.isEmpty()) {
            database.getDao().deleteTrack(trackId)
        }
    }

    override fun getAllTacksForDebugging(): Flow<List<TrackEntity>> {
        return database.getDao().getTracks()
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        val playlistWithSongs = database.getDao().getPlaylistWithTracksSuspend(playlistId)
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
            val size = database.getDao().getTrackWithPlaylists(it.trackId).playlists.size
            if (size == MIN_COUNT_OF_PLAYLISTS_WHERE_TRACK_CAN_BE) {
                countOfDeletedTracks += database.getDao().deleteTrack(it.trackId)
            }
        }
        return countOfDeletedTracks
    }

    private suspend fun clearAll(){
        val songs = database.getDao().clearTracks()
        val playlists = database.getDao().clearPlaylists()
        val crossRef = database.getDao().clearCrossRefs()
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