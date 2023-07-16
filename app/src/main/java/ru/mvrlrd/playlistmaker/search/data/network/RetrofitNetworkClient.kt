package ru.mvrlrd.playlistmaker.search.data.network


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.search.data.NetworkClient
import ru.mvrlrd.playlistmaker.search.data.Response
import ru.mvrlrd.playlistmaker.search.data.TracksSearchRequest
import ru.mvrlrd.playlistmaker.search.data.model.TrackDto

class RetrofitNetworkClient(
    private val itunesService: ItunesApiService,
    private val context: Context
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply {
                resultCode = context.resources.getString(R.string.error_connection_code).toInt()
                errorMessage = context.resources.getString(R.string.error_connection)
            }
        }
        if (dto !is TracksSearchRequest) {
            return Response().apply {
                resultCode = context.resources.getString(R.string.bad_request_code).toInt()
                errorMessage = context.resources.getString(R.string.bad_request)
            }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = itunesService.search(dto.query)

                response.apply { resultCode = context.resources.getString(R.string.success_code).toInt()
                    results = results.filter { it.previewUrl != null } as ArrayList<TrackDto>
                }

            } catch (e: Throwable) {
                Response().apply {
                    resultCode = context.resources.getString(R.string.error_code).toInt()
                    errorMessage = context.resources.getString(R.string.server_error)
                }
            }
        }
    }

    private fun isConnected(): Boolean{
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}