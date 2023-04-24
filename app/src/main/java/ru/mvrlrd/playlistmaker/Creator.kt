package ru.mvrlrd.playlistmaker

import android.content.Context
import ru.mvrlrd.playlistmaker.data.network.RetrofitNetworkClient
import ru.mvrlrd.playlistmaker.data.network.TracksRepositoryImpl
import ru.mvrlrd.playlistmaker.domain.TracksInteractor
import ru.mvrlrd.playlistmaker.domain.TracksRepository
import ru.mvrlrd.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(context: Context): TracksRepository{
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }
     fun provideTracksInteractor(context: Context): TracksInteractor{
        return TracksInteractorImpl(getTracksRepository(context))
    }
}