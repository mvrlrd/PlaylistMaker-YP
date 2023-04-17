package ru.mvrlrd.playlistmaker.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NetworkHandler {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val itunesService = retrofit.create(ItunesApi::class.java)

    companion object{
        private const val BASE_URL = "https://itunes.apple.com"
    }
}