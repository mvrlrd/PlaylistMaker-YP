package ru.mvrlrd.playlistmaker.search.data.network

import com.google.gson.annotations.SerializedName
import ru.mvrlrd.playlistmaker.search.data.Response
import ru.mvrlrd.playlistmaker.search.data.model.TrackDto

class TracksSearchResponse(@SerializedName("results") var results: ArrayList<TrackDto>): Response()