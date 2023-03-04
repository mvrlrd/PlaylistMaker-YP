package ru.mvrlrd.playlistmaker

import ru.mvrlrd.playlistmaker.model.Track

interface TrackOnClickListener {
    fun trackOnClick(track: Track)
}