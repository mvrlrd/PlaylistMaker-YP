package ru.mvrlrd.playlistmaker.data.model

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.data.network.NetworkHandler
import ru.mvrlrd.playlistmaker.data.network.TracksResponse
import ru.mvrlrd.playlistmaker.domain.Track
import ru.mvrlrd.playlistmaker.domain.TrackInteractor
import ru.mvrlrd.playlistmaker.domain.mapTrackModelToTrack

class TrackInteractorImpl(private val networkHandler: NetworkHandler): TrackInteractor {
    private val trackList = mutableListOf<Track>()
    private val trackListLiveData = MutableLiveData<List<Track>>()

    override fun search(query: String) {
        networkHandler.itunesService.search(query)
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>,
                                        response: Response<TracksResponse>
                ) {
//                    progressBar.isVisible = false
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.trackModels?.isNotEmpty() == true) {
                                trackList.clear()
                                trackList.addAll(response.body()?.trackModels!!.map {
                                    it.mapTrackModelToTrack()
                                })
                                updateTrackList()
//                                trackAdapter.setTracks(response.body()?.trackModels!!)
//                                placeHolder.visibility = View.GONE
                            } else {
//                                showMessage(getString(R.string.nothing_found), "")
                            }
                        }
                        401 ->
                            println("401")
//                            showMessage(getString(R.string.authentication_troubles), response.code().toString())
                        else ->
                            println("ERROR")
//                            showMessage(getString(R.string.error_connection), response.code().toString())
                    }
                }
                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    println("onFailure")
//                    progressBar.isVisible = false
//                    showMessage(getString(R.string.error_connection), t.message.toString())
                }
            })
    }

    override fun addToHistory(track: Track) {
        TODO("Not yet implemented")
    }

    override fun getTrackList(): LiveData<List<Track>> {
        return trackListLiveData
    }

    private fun updateTrackList(){
        trackListLiveData.value = trackList.toList()
    }
}