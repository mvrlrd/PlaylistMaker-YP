package ru.mvrlrd.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mvrlrd.playlistmaker.data.NetworkClient
import ru.mvrlrd.playlistmaker.data.Response
import ru.mvrlrd.playlistmaker.data.TracksSearchRequest


class RetrofitNetworkClient(private val context: Context): NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(APPLE_MUSIC_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (!isConnected()){
            return Response().apply { resultCode = NO_INTERNET_CONNECTION_CODE }
        }
        return if (dto is TracksSearchRequest) {
            val resp = itunesService.search(dto.query).execute()
            val body = resp.body() ?: Response()
            body.apply { resultCode = resp.code() }
        }else{
            Response().apply { resultCode = BAD_REQUEST_CODE }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    companion object{
        private const val APPLE_MUSIC_BASE_URL = "https://itunes.apple.com"
    }
}