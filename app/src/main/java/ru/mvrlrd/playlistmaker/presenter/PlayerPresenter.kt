package ru.mvrlrd.playlistmaker.presenter


import ru.mvrlrd.playlistmaker.data.model.MyMediaPlayer
import ru.mvrlrd.playlistmaker.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.domain.Track
import ru.mvrlrd.playlistmaker.formatTime
import ru.mvrlrd.playlistmaker.ui.PlayerView
import ru.mvrlrd.playlistmaker.presenter.PlayerState.*

class PlayerPresenter(track: Track, private val playerView: PlayerView) {
    private val myMediaPlayer: PlayerInteractor = MyMediaPlayer(track)
    private var playerState: PlayerState = STATE_DEFAULT
    init {
//        onComplete()
    }

    private fun start() {
        myMediaPlayer.start()
        playerState = STATE_PLAYING
        playerView.onClickPlayButton(playerState)
//        handler.postDelayed(timerGo, REFRESH_TIMER_DELAY_MILLIS)
    }
    fun pause() {
        myMediaPlayer.pause()
        playerState = STATE_PAUSED
        playerView.onClickPlayButton(playerState)
//        handler.removeCallbacks(timerGo)
    }



    fun onDestroy(){
        myMediaPlayer.onDestroy()
    }

    fun getCurrentPosition():String{
//        return "formatTime(myMediaPlayer.getCurrentTime())"
        return "00:03"
    }

    private fun onComplete(){
        myMediaPlayer.setOnCompletionListener {
            println("_____))_))___))__))_")
            playerState = STATE_PREPARED
            playerView.onClickPlayButton(playerState)

//            handler.removeCallbacks(timerGo)
//            clockText.text = resources.getText(R.string.null_timer)
        }
    }



    fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pause()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                start()
            }
            STATE_DEFAULT -> {

            }
        }
    }

}

enum class PlayerState{
    STATE_DEFAULT,
    STATE_PREPARED,
    STATE_PLAYING,
    STATE_PAUSED
}