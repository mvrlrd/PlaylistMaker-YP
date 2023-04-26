package ru.mvrlrd.playlistmaker

import android.content.Context
import ru.mvrlrd.playlistmaker.search.data.TracksRepositoryImpl
import ru.mvrlrd.playlistmaker.search.data.network.RetrofitNetworkClient
import ru.mvrlrd.playlistmaker.search.data.storage.LocalStorage
import ru.mvrlrd.playlistmaker.search.domain.TracksInteractor
import ru.mvrlrd.playlistmaker.search.domain.TracksRepository
import ru.mvrlrd.playlistmaker.search.domain.impl.TracksInteractorImpl

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
}