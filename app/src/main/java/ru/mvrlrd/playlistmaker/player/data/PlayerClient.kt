package ru.mvrlrd.playlistmaker.player.data

import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer

interface PlayerClient {
     fun preparePlayer(trackForPlayer: TrackForPlayer, prepare: () -> Unit)

     fun setOnCompletionListener(onComplete: () -> Unit)

     fun start()

     fun pause()

     fun onDestroy()

     fun getCurrentTime(): Int
}