package ru.mvrlrd.playlistmaker.data

interface NetworkClient {
    fun doRequest(dto: Any): Response
}