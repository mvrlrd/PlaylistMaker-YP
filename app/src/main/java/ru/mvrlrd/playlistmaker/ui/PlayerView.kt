package ru.mvrlrd.playlistmaker.ui

import ru.mvrlrd.playlistmaker.presenter.PlayerState

interface PlayerView {
    fun onClickPlayButton(state: PlayerState)
    fun refreshTimer(time: String)

}