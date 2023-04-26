package ru.mvrlrd.playlistmaker.presenter



import ru.mvrlrd.playlistmaker.formatTime
import ru.mvrlrd.playlistmaker.player.ui.PlayerView
import ru.mvrlrd.playlistmaker.presenter.PlayerState.*
import ru.mvrlrd.playlistmaker.search.data.model.MyMediaPlayer
import ru.mvrlrd.playlistmaker.search.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.search.domain.Track

class PlayerPresenter(track: Track, private val playerView: PlayerView) {
    private val myMediaPlayer: PlayerInteractor = MyMediaPlayer(track)
    private var playerState: PlayerState = STATE_DEFAULT
    init {
        preparePlayer()
        setOnCompletionListener()
    }

    private fun start() {
        myMediaPlayer.start()
        playerState = STATE_PLAYING
        playerView.handlePlayButton(playerState)
        playerView.startPostDelay()
    }
    fun pause() {
        myMediaPlayer.pause()
        playerState = STATE_PAUSED
        playerView.handlePlayButton(playerState)
        playerView.removePostDelay()
    }

    fun onDestroy(){
        myMediaPlayer.onDestroy()
    }

    fun getCurrentPosition():String{
        return formatTime(myMediaPlayer.getCurrentTime())

    }

    private fun setOnCompletionListener() {
        myMediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            playerView.handlePlayButton(playerState)
            playerView.onCompletePlaying()
        }
    }

    private fun preparePlayer(){
        myMediaPlayer.preparePlayer {
            playerView.enablePlayButton()
            playerState = STATE_PREPARED
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