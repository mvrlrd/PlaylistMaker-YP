package ru.mvrlrd.playlistmaker.search.data.player.domain

interface PlayerInteractor {
    fun start()
    fun pause()
    fun onDestroy()
    fun setOnCompletionListener(onComplete: ()->Unit)
    fun getCurrentTime(): Int
    fun preparePlayer(prepare: () -> Unit)
}