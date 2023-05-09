package ru.mvrlrd.playlistmaker.search.util

import ru.mvrlrd.playlistmaker.search.data.Response

sealed class Resource<T> (val data: T? = null, val response: Response){
    class Success<T>(data: T, response: Response): Resource<T>(data, response = response)
    class Error<T>(response: Response, data: T? = null): Resource<T>(data = data, response = response)
}