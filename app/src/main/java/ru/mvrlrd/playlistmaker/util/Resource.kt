package ru.mvrlrd.playlistmaker.util

sealed class Resource<T> (val data: T? = null, val message: String? = null, val code: Int){
    class Success<T>(data: T, code: Int): Resource<T>(data, code = code)
    class Error<T>(message: String, data: T? = null, code: Int): Resource<T>(data, message, code)
}