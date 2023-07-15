package ru.mvrlrd.playlistmaker.player.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mvrlrd.playlistmaker.database.data.FavoriteDb
import ru.mvrlrd.playlistmaker.database.data.TrackConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.domain.PlayerRepository
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.playlistDb.data.PlaylistConverter
import ru.mvrlrd.playlistmaker.playlistDb.data.PlaylistDb

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

    override fun getLivePlayerState():LiveData<MyMediaPlayer.PlayerState>{
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

    override suspend fun removeFromFavorite(trackId: Int) {
        favoriteDb.getDao().deleteTrack(trackId)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistForAdapter>> {
        return playlistDb.getDao().getAllPlaylists().map { list ->
            playlistConverter.convertEntityListToAdapterList(list)
        }
    }
}