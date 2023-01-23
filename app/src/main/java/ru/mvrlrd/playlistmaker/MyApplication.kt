package ru.mvrlrd.playlistmaker

import android.app.Application

import ru.mvrlrd.playlistmaker.model.TrackDb

class MyApplication : Application() {
        val  trackDb : TrackDb = TrackDb()
        companion object{

        }
}