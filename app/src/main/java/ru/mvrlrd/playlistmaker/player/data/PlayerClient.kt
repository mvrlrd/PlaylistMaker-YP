package ru.mvrlrd.playlistmaker.player.data

import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack

interface PlayerClient {
     fun preparePlayer(playerTrack: PlayerTrack, prepare: () -> Unit)

     fun setOnCompletionListener(onComplete: () -> Unit)

     fun start()

     fun pause()

     fun onDestroy()

     fun getCurrentTime(): Int
}