package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistDb
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

    override fun removeTrackFromPlaylist(trackId: Long, playlistId: Long): Flow<Int> {
        return flow {
            emit(playlistDb.getDao().deleteTrack(trackId, playlistId))
        }

    }
}