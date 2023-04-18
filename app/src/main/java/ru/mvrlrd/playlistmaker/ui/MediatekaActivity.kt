package ru.mvrlrd.playlistmaker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayout
import ru.mvrlrd.playlistmaker.R

class MediatekaActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var myTab: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mediateka)

        toolbar = findViewById<Toolbar>(R.id.mediatekaToolbar).apply {
            setNavigationOnClickListener { onBackPressed() }
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