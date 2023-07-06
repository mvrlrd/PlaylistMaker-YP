package ru.mvrlrd.playlistmaker.player.domain

interface PlayerRepository {
    fun preparePlayer(playerTrack: PlayerTrack, prepare: () -> Unit)

    fun setOnCompletionListener(onComplete: () -> Unit)

    fun start()

    fun pause()

    fun onDestroy()

    fun getCurrentTime(): Int

    suspend fun addToFavorite(playerTrack: PlayerTrack)


    suspend fun removeFromFavorite(trackId: Int)

}