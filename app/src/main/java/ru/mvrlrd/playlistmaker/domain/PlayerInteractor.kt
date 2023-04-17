package ru.mvrlrd.playlistmaker.domain

interface PlayerInteractor {
    fun start()
    fun pause()
    fun onDestroy()
    fun setOnCompletionListener(onComplete: ()->Unit)
    fun getCurrentTime(): Int
}