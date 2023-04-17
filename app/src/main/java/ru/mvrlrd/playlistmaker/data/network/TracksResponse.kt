package ru.mvrlrd.playlistmaker.data.network

import com.google.gson.annotations.SerializedName
import ru.mvrlrd.playlistmaker.data.model.Track

class TracksResponse(@SerializedName("results") val tracks: ArrayList<Track>)