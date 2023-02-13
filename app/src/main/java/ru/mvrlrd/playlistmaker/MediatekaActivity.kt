package ru.mvrlrd.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.tabs.TabLayout

class MediatekaActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var myTab: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mediateka)



        backButton = findViewById<ImageView?>(R.id.backButton).apply {
            setOnClickListener {
                onBackPressed()
            }
        }

        myTab = findViewById<TabLayout>(R.id.mediatekaTab).apply {
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab!!.position) {
                        0 -> {println("0")}
                        1 -> {println("1")}
                    }
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
}