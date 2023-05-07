package ru.mvrlrd.playlistmaker.search.data.network.interceptor

import ru.mvrlrd.playlistmaker.search.data.network.INTERNET_CONNECTION_MESSAGE
import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String
        get() = INTERNET_CONNECTION_MESSAGE

}