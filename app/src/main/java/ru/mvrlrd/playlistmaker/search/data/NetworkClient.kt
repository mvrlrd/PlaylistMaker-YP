package ru.mvrlrd.playlistmaker.search.data

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}