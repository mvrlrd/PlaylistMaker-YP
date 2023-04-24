package ru.mvrlrd.playlistmaker.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mvrlrd.playlistmaker.data.NetworkClient
import ru.mvrlrd.playlistmaker.data.Response
import ru.mvrlrd.playlistmaker.data.TracksSearchRequest


class RetrofitNetworkClient: NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(APPLE_MUSIC_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            val resp = itunesService.search(dto.query).execute()
            val body = resp.body() ?: Response()
            body.apply { resultCode = resp.code() }
        }else{
            Response().apply { resultCode = BAD_REQUEST_CODE }
        }
    }

    companion object{
        private const val APPLE_MUSIC_BASE_URL = "https://itunes.apple.com"
    }
}