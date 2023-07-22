package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import ru.mvrlrd.playlistmaker.mediateka.favorites.data.favs_db.FavoriteDb
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistDb
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.Song
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.relations.PlaylistWithSongs
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfo
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfoRepository
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

class PlaylistInfoRepositoryImpl(
    private val playlistDb: PlaylistDb, private val favoriteDb: FavoriteDb,
    private val converter: PlaylistConverter
) : PlaylistInfoRepository {
    override fun getPlaylistWithSongs(id: Long): Flow<PlaylistInfo> {
        return playlistDb.getDao().getPlaylistWithSongs(id)
            .map {
                mapPlaylistWithSongsToPlaylistInfo(it)
            }
    }

    private fun mapPlaylistWithSongsToPlaylistInfo(playlist: PlaylistWithSongs): PlaylistInfo {
        return PlaylistInfo(
            playlist = converter.mapEntityToAdapter(playlist.playlist),
            songs = converter.mapSongsToTracks(playlist.songs)
        )
    }
    override fun getFavsIds(): Flow<List<Long>> {
        return favoriteDb.getDao().getFavIds2()
    }

}