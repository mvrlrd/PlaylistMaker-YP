package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.data

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistDb
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.Song
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfo
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfoRepository


class PlaylistInfoRepositoryImpl(
    private val playlistDb: PlaylistDb,
    private val converter: PlaylistConverter
) : PlaylistInfoRepository {
    override fun getPlaylistWithSongs(id: Long): Flow<PlaylistInfo> {
        return playlistDb.getDao().getPlaylistWithSongs(id)
            .map {
                converter.mapPlaylistWithSongsToPlaylistInfo(it)
            }
    }

   override suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long): Flow<Int>{
        val flowable =  playlistDb.getDao().deleteTrack(trackId, playlistId)
        val songWithPlaylists = playlistDb.getDao().getSongWithPlaylists(trackId)
        if (songWithPlaylists.playlists.isEmpty()){
            playlistDb.getDao().deleteSong(trackId)
        }
        return flow { emit(flowable)}
    }

    override fun getAllSongsForDebug(): Flow<List<Song>> {
        return playlistDb.getDao().getSongs()
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        TODO("Not yet implemented")
    }
}