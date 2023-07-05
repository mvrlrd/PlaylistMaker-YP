package ru.mvrlrd.playlistmaker.player.data

import ru.mvrlrd.playlistmaker.database.data.FavoriteDb
import ru.mvrlrd.playlistmaker.database.data.TrackConverter
import ru.mvrlrd.playlistmaker.player.domain.PlayerRepository
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer

class PlayerRepositoryImpl(
    private val playerClient: PlayerClient,
    private val favoriteDb: FavoriteDb,
    private val trackConverter: TrackConverter
) : PlayerRepository {
    override fun preparePlayer(trackForPlayer: TrackForPlayer, prepare: () -> Unit) {
        playerClient.preparePlayer(trackForPlayer, prepare)
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        playerClient.setOnCompletionListener(onComplete)
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

    override fun getCurrentTime(): Int {
        return playerClient.getCurrentTime()
    }

    override suspend fun addToFavorite(trackForPlayer: TrackForPlayer) {
        favoriteDb.getDao().insertTrack(trackConverter.mapToEntity(trackForPlayer))
    }

    override suspend fun removeFromFavorite(trackId: Int) {
        favoriteDb.getDao().deleteTrack(trackId)
    }
}