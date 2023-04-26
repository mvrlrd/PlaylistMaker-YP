package ru.mvrlrd.playlistmaker.player.data

import android.media.MediaPlayer
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer

class MyMediaPlayer(track: TrackForPlayer): PlayerInteractor {
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    init {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
    }


    override fun preparePlayer(prepare: () -> Unit) {
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

