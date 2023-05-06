package ru.mvrlrd.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mvrlrd.playlistmaker.search.data.NetworkClient
import ru.mvrlrd.playlistmaker.search.data.TracksSearchRequest


class RetrofitNetworkClient(private val context: Context,
                            private val itunesService:ItunesApiService) : NetworkClient {

//    private val retrofit = Retrofit.Builder()
//        .baseUrl(APPLE_MUSIC_BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()

//    private val itunesService = retrofit.create(ItunesApiService::class.java)

    override fun doRequest(dto: Any): ru.mvrlrd.playlistmaker.search.data.Response {
        if (!isConnected()){
            return ru.mvrlrd.playlistmaker.search.data.Response()
                .apply { resultCode = NO_INTERNET_CONNECTION_CODE }
        }
        return if (dto is TracksSearchRequest) {
            val resp = itunesService.search(dto.query).execute()
            val body = resp.body() ?: ru.mvrlrd.playlistmaker.search.data.Response()
            body.apply { resultCode = resp.code() }
        }else{
            ru.mvrlrd.playlistmaker.search.data.Response().apply { resultCode = BAD_REQUEST_CODE }
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