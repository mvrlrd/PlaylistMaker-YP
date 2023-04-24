package ru.mvrlrd.playlistmaker

import ru.mvrlrd.playlistmaker.data.network.RetrofitNetworkClient
import ru.mvrlrd.playlistmaker.data.network.TracksRepositoryImpl
import ru.mvrlrd.playlistmaker.domain.TracksInteractor
import ru.mvrlrd.playlistmaker.domain.TracksRepository
import ru.mvrlrd.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository{
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }
     fun provideTracksInteractor(): TracksInteractor{
        return TracksInteractorImpl(getTracksRepository())
    }
}