package ru.mvrlrd.playlistmaker.mediateka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import ru.mvrlrd.playlistmaker.databinding.ActivityMediatekaBinding

class MediatekaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediatekaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mediatekaToolbar.apply {
            setNavigationOnClickListener { onBackPressed() }
        }


        binding.mediatekaTab.apply {
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