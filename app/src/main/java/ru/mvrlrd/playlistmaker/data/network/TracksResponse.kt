package ru.mvrlrd.playlistmaker.data.network

import com.google.gson.annotations.SerializedName
import ru.mvrlrd.playlistmaker.data.model.TrackModel

class TracksResponse(@SerializedName("results") val trackModels: ArrayList<TrackModel>)