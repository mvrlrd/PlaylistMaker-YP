package ru.mvrlrd.playlistmaker.search.data.network


import ru.mvrlrd.playlistmaker.search.data.NetworkClient
import ru.mvrlrd.playlistmaker.search.data.Response
import ru.mvrlrd.playlistmaker.search.data.TracksSearchRequest
import ru.mvrlrd.playlistmaker.search.data.network.interceptor.NoConnectivityException

class RetrofitNetworkClient(
    private val itunesService: ItunesApiService
) : NetworkClient {
    override fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            try {
                val resp = itunesService.search(dto.query).execute()
                val body = resp.body() ?: Response()
                body.apply { resultCode = resp.code() }
            } catch (e: NoConnectivityException) {
                return Response()
                    .apply { resultCode = NO_INTERNET_CONNECTION_CODE }
            }
        } else {
            Response().apply { resultCode = BAD_REQUEST_CODE }
        }
    }
}