package ru.mvrlrd.playlistmaker.data.network

import com.google.gson.annotations.SerializedName
import ru.mvrlrd.playlistmaker.data.Response
import ru.mvrlrd.playlistmaker.data.model.TrackDto

class TracksSearchResponse(@SerializedName("results") val results: ArrayList<TrackDto>): Response()