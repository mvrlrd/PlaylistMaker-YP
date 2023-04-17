package ru.mvrlrd.playlistmaker.data.model

import android.media.MediaPlayer
import ru.mvrlrd.playlistmaker.R

import ru.mvrlrd.playlistmaker.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.domain.Track

class MyMediaPlayer(track: Track): PlayerInteractor {
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    init {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
////          playButton.isEnabled = true
//            playerState = STATE_PREPARED
        }

    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
//        mediaPlayer.setOnCompletionListener {
////            onComplete()
//        }
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

