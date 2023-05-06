package ru.mvrlrd.playlistmaker.player.domain

interface PlayerRepository {
    fun preparePlayer(trackForPlayer: TrackForPlayer, prepare: () -> Unit)

    fun setOnCompletionListener(onComplete: () -> Unit)

    fun start()

    fun pause()

    fun onDestroy()

    fun getCurrentTime(): Int
}