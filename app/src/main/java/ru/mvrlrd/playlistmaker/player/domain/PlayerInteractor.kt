package ru.mvrlrd.playlistmaker.player.domain

interface PlayerInteractor {
    fun start()
    fun pause()
    fun onDestroy()
    fun setOnCompletionListener(onComplete: ()->Unit)
    fun getCurrentTime(): Int
    fun preparePlayer(playerTrack: PlayerTrack, prepare: () -> Unit)

    suspend fun addTrackToFavorite(playerTrack: PlayerTrack)

    suspend fun removeTrackFromFavorite(trackId: Int)

}