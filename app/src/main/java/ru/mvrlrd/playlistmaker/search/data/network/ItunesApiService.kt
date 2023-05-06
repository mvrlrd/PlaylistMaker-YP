package ru.mvrlrd.playlistmaker.search.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String) : Call<TracksSearchResponse>
}