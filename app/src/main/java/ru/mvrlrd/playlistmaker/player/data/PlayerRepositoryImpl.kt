package ru.mvrlrd.playlistmaker.player.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.mvrlrd.playlistmaker.mediateka.favorites.data.favs_db.FavoriteDb
import ru.mvrlrd.playlistmaker.mediateka.favorites.data.favs_db.TrackConverter
import ru.mvrlrd.playlistmaker.mediateka.favorites.data.favs_db.TrackEntity
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.domain.PlayerRepository
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistDb
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.PlaylistSongCrossRef
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.Song
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.relations.PlaylistWithSongs
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter
import java.util.*

class PlayerRepositoryImpl(
    private val playerClient: PlayerClient,
    private val favoriteDb: FavoriteDb,
    private val playlistDb: PlaylistDb,
    private val playlistConverter: PlaylistConverter,
    private val trackConverter: TrackConverter
) : PlayerRepository {
    override fun preparePlayer(playerTrack: PlayerTrack) {
        playerClient.preparePlayer(playerTrack)
    }

    override fun getLivePlayerState(): LiveData<MyMediaPlayer.PlayerState> {
        return playerClient.getLivePlayerState()
    }

    override fun start() {
        playerClient.start()
    }

    override fun pause() {
        playerClient.pause()
    }

    override fun onDestroy() {
        playerClient.onDestroy()
    }

    override fun getCurrentTime(): Flow<Int> {
        return playerClient.getCurrentTime()
    }

    override suspend fun addToFavorite(playerTrack: PlayerTrack) {
        favoriteDb.getDao().insertTrack(trackConverter.mapPlayerTrackToEntity(playerTrack))
    }

    override suspend fun removeFromFavorite(trackId: Long) {
        favoriteDb.getDao().deleteTrack(trackId)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistForAdapter>> {
        return playlistDb.getDao().getAllPlaylists().map { list ->
            playlistConverter.convertEntityListToAdapterList(list)
        }
    }

    override suspend fun addTrackToPlaylist(
        trackId: TrackForAdapter,
        playlistId: Long
    ): Flow<Pair<String, Boolean>> {
        playlistDb.getDao().insertTrack(mapTrackForAdapterToSong(trackId))
        val playerSongCrossRef = PlaylistSongCrossRef(songId = trackId.trackId, playlistId = playlistId)
        val playlistName = playlistDb.getDao().getPlaylist(playlistId).name
        return flow {
            emit(
                playlistName to (playlistDb.getDao()
                    .insertPlaylistSongCrossRef(playerSongCrossRef) != -1L)
            )
        }
    }

    private fun mapTrackForAdapterToSong(track : TrackForAdapter): Song{
        return with(track){
            Song(songId = trackId,
                trackName= trackName,
                artistName = artistName,
               image =  image,
                album = album,
                year = year,
                trackTime = trackTime,
                genre = genre,
                country = country,
                previewUrl = previewUrl, date = Calendar.getInstance().timeInMillis)
        }
    }

    override fun getAllPlaylistsWithSongs(): Flow<List<PlaylistForAdapter>> {
        return playlistDb.getDao().getPlaylistsWithSongs().map {
            mapListDaoToListForAdapter(it)
        }
    }

    private fun mapListDaoToListForAdapter(daoList: List<PlaylistWithSongs>): List<PlaylistForAdapter> {
        return daoList.map {
            PlaylistForAdapter(
                playlistId = it.playlist.playlistId,
                name = it.playlist.name,
                description = it.playlist.description,
                playlistImagePath = it.playlist.playlistImagePath,
                tracksQuantity = it.songs.size
            )
        }
    }
}