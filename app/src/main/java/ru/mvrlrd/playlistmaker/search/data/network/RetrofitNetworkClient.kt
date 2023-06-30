package ru.mvrlrd.playlistmaker.search.data.network


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
            if (dto is TracksSearchRequest) {
                return withContext(Dispatchers.IO) {
                    try {
                        val response = itunesService.search(dto.query)
                        response.apply { resultCode = 200 }
                    } catch (e: Throwable) {
                        Response().apply { resultCode = 500 }
                    }
                }
            } else {
                throw IOException("REQUEST IS NOT TracksSearchRequest")
            }
        }catch (e: InterruptedIOException){
            throw InterruptedIOException(" call Timeout finished")
        }
    }
}