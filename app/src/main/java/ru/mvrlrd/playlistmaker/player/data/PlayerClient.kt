package ru.mvrlrd.playlistmaker.player.data

interface PlayerClient {
     fun preparePlayer(prepare: () -> Unit)

     fun setOnCompletionListener(onComplete: () -> Unit)

     fun start()

     fun pause()

     fun onDestroy()

     fun getCurrentTime(): Int
}