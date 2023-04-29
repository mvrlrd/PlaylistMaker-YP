package ru.mvrlrd.playlistmaker.search.data

interface NetworkClient {
    fun doRequest(dto: Any): Response
}