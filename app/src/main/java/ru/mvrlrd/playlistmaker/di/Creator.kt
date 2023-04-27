package ru.mvrlrd.playlistmaker.di

import android.content.Context
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer
import ru.mvrlrd.playlistmaker.player.data.PlayerRepositoryImpl
import ru.mvrlrd.playlistmaker.player.domain.PlayerInteractor
import ru.mvrlrd.playlistmaker.player.domain.PlayerRepository
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer
import ru.mvrlrd.playlistmaker.player.domain.impl.PlayerInteractorImpl
import ru.mvrlrd.playlistmaker.search.data.TracksRepositoryImpl
import ru.mvrlrd.playlistmaker.search.data.network.RetrofitNetworkClient
import ru.mvrlrd.playlistmaker.search.data.storage.LocalStorage
import ru.mvrlrd.playlistmaker.search.domain.TracksInteractor
import ru.mvrlrd.playlistmaker.search.domain.TracksRepository
import ru.mvrlrd.playlistmaker.search.domain.impl.TracksInteractorImpl
import ru.mvrlrd.playlistmaker.settings.data.ThemeSwitchRepositoryImpl
import ru.mvrlrd.playlistmaker.settings.data.storage.ThemeStorage
import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchInteractor
import ru.mvrlrd.playlistmaker.settings.domain.ThemeSwitchRepository
import ru.mvrlrd.playlistmaker.settings.domain.impl.ThemeSwitchInteractorImpl

object Creator {
    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient(context),
            LocalStorage(context.getSharedPreferences("local_storage", Context.MODE_PRIVATE))
        )
    }
     fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }
    private fun getPlayerRepository(trackForPlayer: TrackForPlayer): PlayerRepository{
        return PlayerRepositoryImpl(MyMediaPlayer(trackForPlayer))
    }
    fun providePlayerInteractor(trackForPlayer: TrackForPlayer): PlayerInteractor{
        return PlayerInteractorImpl(getPlayerRepository(trackForPlayer))

    }


    private fun getThemeSwitchRepository(context: Context): ThemeSwitchRepository {
        return ThemeSwitchRepositoryImpl(
            ThemeStorage(context.getSharedPreferences("local_storage", Context.MODE_PRIVATE))
        )
    }
    fun provideThemeSwitchInteractor(context: Context): ThemeSwitchInteractor {
        return ThemeSwitchInteractorImpl(getThemeSwitchRepository(context))
    }

}