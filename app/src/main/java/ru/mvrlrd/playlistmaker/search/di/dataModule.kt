package ru.mvrlrd.playlistmaker.search.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import ru.mvrlrd.playlistmaker.search.data.ILocalStorage
import ru.mvrlrd.playlistmaker.search.data.NetworkClient
import ru.mvrlrd.playlistmaker.search.data.network.ItunesApiService
import ru.mvrlrd.playlistmaker.search.data.network.RetrofitNetworkClient
import ru.mvrlrd.playlistmaker.search.data.storage.LocalStorage

val searchLocalStorageDataModule = module {
    single<ILocalStorage> {
        LocalStorage(sharedPreferences = get())
    }
    single<SharedPreferences> {androidContext().getSharedPreferences(LOCAL_STORAGE, Context.MODE_PRIVATE)}
}

val searchNetworkDataModule = module {
    single<NetworkClient> {
        RetrofitNetworkClient(context = androidContext(), itunesService = get())
    }
    single<ItunesApiService> {
        Retrofit.Builder()
            .baseUrl(APPLE_MUSIC_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApiService::class.java)
    }
}

private const val LOCAL_STORAGE = "local_storage"
private const val APPLE_MUSIC_BASE_URL = "https://itunes.apple.com"