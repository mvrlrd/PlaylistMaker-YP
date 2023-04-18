package ru.mvrlrd.playlistmaker.retrofit

import com.google.gson.annotations.SerializedName
import ru.mvrlrd.playlistmaker.model.Track

class TracksResponse(@SerializedName("results") val tracks: ArrayList<Track>)