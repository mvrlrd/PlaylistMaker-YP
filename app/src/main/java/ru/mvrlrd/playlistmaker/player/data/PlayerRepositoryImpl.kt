package ru.mvrlrd.playlistmaker.player.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.mvrlrd.playlistmaker.database.data.FavoriteDb
import ru.mvrlrd.playlistmaker.database.data.TrackConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.domain.PlayerRepository
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.db_playlist.PlaylistConverter
import ru.mvrlrd.playlistmaker.db_playlist.PlaylistDb
import ru.mvrlrd.playlistmaker.db_playlist.entities.PlaylistSongCrossRef
import ru.mvrlrd.playlistmaker.db_playlist.entities.Song
import ru.mvrlrd.playlistmaker.db_playlist.relations.PlaylistWithSongs

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
        trackId: Long,
        playlistId: Long
    ): Flow<Pair<String, Boolean>> {
        playlistDb.getDao().insertTrack(Song(trackId))
        val playerSongCrossRef = PlaylistSongCrossRef(songId = trackId, playlistId = playlistId)
        val playlistName = playlistDb.getDao().getPlaylist(playlistId).name
        return flow {
            emit(
                playlistName to (playlistDb.getDao()
                    .insertPlaylistSongCrossRef(playerSongCrossRef) != -1L)
            )
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