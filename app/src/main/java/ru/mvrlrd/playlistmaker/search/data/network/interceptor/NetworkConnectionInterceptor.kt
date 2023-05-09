package ru.mvrlrd.playlistmaker.search.data.network.interceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import ru.mvrlrd.playlistmaker.R
import java.net.ConnectException
import java.net.SocketTimeoutException


class NetworkConnectionInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            if (!isConnected) {
                throw NoConnectivityException()
            }
            val builder: Request.Builder = request.newBuilder()
            return chain.proceed(builder.build())
        } catch (e: Exception) {
            val message = context.resources.getString(R.string.error_connection)
            val code = when (e) {
                is ConnectException -> {
                    CONNECTION_ERROR
                }
                is NoConnectivityException -> {
                    NO_INTERNET_CONNECTION_CODE
                }
                is SocketTimeoutException -> {
                    SOCKET_TIME_OUT_EXCEPTION
                }
                else -> {
                    BAD_REQUEST_CODE
                }
            }
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .message(message)
                .body(ResponseBody.create(null, "{${e}}"))
                .build()
        }
    }

    private val isConnected: Boolean
        get() {
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

    companion object{
        const val BAD_REQUEST_CODE = 400
        const val CONNECTION_ERROR = 10
        const val SOCKET_TIME_OUT_EXCEPTION = 20
        const val NO_INTERNET_CONNECTION_CODE = 30
    }
}
