package ru.mvrlrd.playlistmaker.data

import ru.mvrlrd.playlistmaker.data.model.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}