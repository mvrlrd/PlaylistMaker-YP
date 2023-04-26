package ru.mvrlrd.playlistmaker.player.ui

import ru.mvrlrd.playlistmaker.presenter.PlayerState

interface PlayerView {
    fun handlePlayButton(state: PlayerState)
//    fun updateTimer(time: String)

    fun enablePlayButton()

    fun onCompletePlaying()

    fun removePostDelay()

    fun startPostDelay()

}