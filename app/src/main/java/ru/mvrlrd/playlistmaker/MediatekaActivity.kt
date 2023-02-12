package ru.mvrlrd.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import ru.mvrlrd.playlistmaker.model.Track
import ru.mvrlrd.playlistmaker.recycler.TrackAdapter

class MediatekaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mediateka)

        val myApplication: MyApplication = MyApplication()

        val trackAdapter = TrackAdapter().apply {
            tracks = myApplication.trackDb.favoriteTracks as MutableList<Track>
        }
        val recyclerView = findViewById<RecyclerView>(R.id.mediatekaTracksRecyclerView).apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

    val myTab = findViewById<TabLayout>(R.id.mediatekaTab)
        myTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position){
                    0 -> {trackAdapter.tracks = myApplication.trackDb.favoriteTracks as MutableList<Track>}
                    1 -> {trackAdapter.tracks = myApplication.trackDb.allTracks as MutableList<Track>}
                }
                trackAdapter.notifyDataSetChanged()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                println("onTabReselected")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                println("onTabUnselected")
            }
        })
    }



}