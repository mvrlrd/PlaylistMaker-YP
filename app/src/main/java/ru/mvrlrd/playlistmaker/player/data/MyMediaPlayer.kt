package ru.mvrlrd.playlistmaker.player.data

import android.media.MediaPlayer
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer

class MyMediaPlayer(private val mediaPlayer: MediaPlayer): PlayerClient {

    override fun preparePlayer(trackForPlayer: TrackForPlayer, prepare: () -> Unit) {
        mediaPlayer.setDataSource(trackForPlayer.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { prepare() }
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            onComplete()
        }
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        mediaPlayer.release()
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }
}

