package ru.mvrlrd.playlistmaker.search.util

sealed class Resource<T> (val data: T? = null, val responseCode: Int, val message: String? = null){
    class Success<T>(data: T, responseCode: Int): Resource<T>(data, responseCode = responseCode)
    class Error<T>(responseCode: Int, data: T? = null, message: String): Resource<T>(data = data, responseCode = responseCode, message = message)
}