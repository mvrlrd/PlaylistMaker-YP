package ru.mvrlrd.playlistmaker.player.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.koin.java.KoinJavaComponent.inject
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor

class MyBroadcastReceiver: BroadcastReceiver() {
    private val interactor: PlayerInteractor by inject(PlayerInteractor::class.java)


    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "onReceive: ______________" )
        interactor.handleStartAndPause()
    }

    companion object{
        private const val TAG = "MyBroadcastReceiver"
    }
}