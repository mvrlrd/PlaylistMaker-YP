package ru.mvrlrd.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.mvrlrd.playlistmaker.model.Track
import ru.mvrlrd.playlistmaker.model.TrackDb
import ru.mvrlrd.playlistmaker.recycler.TrackAdapter

class MediatekaActivity : AppCompatActivity() {
    private val _tracks = TrackDb().tracks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mediateka)

        val trackAdapter = TrackAdapter().apply {
            tracks = _tracks as MutableList<Track>
        }
        val recyclerView = findViewById<RecyclerView>(R.id.mediatekaTracksRecyclerView).apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }


}