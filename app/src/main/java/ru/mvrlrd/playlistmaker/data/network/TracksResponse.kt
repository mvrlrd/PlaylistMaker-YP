package ru.mvrlrd.playlistmaker.data.network

import com.google.gson.annotations.SerializedName
import ru.mvrlrd.playlistmaker.data.model.TrackDto

class TracksResponse(@SerializedName("results") val trackDtos: ArrayList<TrackDto>)