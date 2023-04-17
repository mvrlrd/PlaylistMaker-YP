package ru.mvrlrd.playlistmaker.network

import com.google.gson.annotations.SerializedName
import ru.mvrlrd.playlistmaker.model.Track

class TracksResponse(@SerializedName("results") val tracks: ArrayList<Track>)