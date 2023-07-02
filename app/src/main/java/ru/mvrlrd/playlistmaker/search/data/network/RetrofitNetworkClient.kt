package ru.mvrlrd.playlistmaker.search.data.network


import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mvrlrd.playlistmaker.search.data.NetworkClient
import ru.mvrlrd.playlistmaker.search.data.Response
import ru.mvrlrd.playlistmaker.search.data.TracksSearchRequest
import java.io.IOException
import java.io.InterruptedIOException

class RetrofitNetworkClient(
    private val itunesService: ItunesApiService
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
         try {
             Log.d(TAG, "1")
            if (dto is TracksSearchRequest) {
                return withContext(Dispatchers.IO) {
                    try {
                        val response = itunesService.search(dto.query)
                        Log.d(TAG, response.results.toString())
                        response.apply { resultCode = 200 }
                    } catch (e: Throwable) {
                        Log.d(TAG, "response.results.toString()")
                        Response().apply {
                            resultCode = 500
                            errorMessage = "REQUEST IS NOT TracksSearchRequest"
                        }
                    }
                }
            } else {
                throw IOException("REQUEST IS NOT TracksSearchRequest")
            }
        }catch (e: InterruptedIOException){
            throw InterruptedIOException(" call Timeout finished")
        }
    }
    companion object{
        private const val TAG = "RetrofitNetworkClient"
    }

}