package ru.mvrlrd.playlistmaker.player.data

import android.media.MediaPlayer
import android.util.Log
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack

class MyMediaPlayer(private val mediaPlayer: MediaPlayer): PlayerClient {

    override fun preparePlayer(playerTrack: PlayerTrack, prepare: () -> Unit) {
        mediaPlayer.setDataSource(playerTrack.previewUrl)
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
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
        }
    }

    override fun onDestroy() {
        mediaPlayer.reset()
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }
}

