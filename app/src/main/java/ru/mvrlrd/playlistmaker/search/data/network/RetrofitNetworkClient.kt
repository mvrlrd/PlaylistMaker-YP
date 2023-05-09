package ru.mvrlrd.playlistmaker.search.data.network


import ru.mvrlrd.playlistmaker.search.data.NetworkClient
import ru.mvrlrd.playlistmaker.search.data.Response
import ru.mvrlrd.playlistmaker.search.data.TracksSearchRequest
import java.io.IOException
import java.io.InterruptedIOException

class RetrofitNetworkClient(
    private val itunesService: ItunesApiService
) : NetworkClient {
    override fun doRequest(dto: Any): Response {
        return try {
            if (dto is TracksSearchRequest) {
                val resp = itunesService.search(dto.query).execute()
                val body = resp.body() ?: Response()
                body.apply {
                    resultCode = resp.code()
                    errorMessage = resp.message()
                }
            } else {
                throw IOException("REQUEST IS NOT TracksSearchRequest")
            }
        }catch (e: InterruptedIOException){
            throw InterruptedIOException(" call Timeout finished")
        }

    }
}