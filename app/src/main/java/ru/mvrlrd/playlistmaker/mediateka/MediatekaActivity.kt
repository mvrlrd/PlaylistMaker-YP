package ru.mvrlrd.playlistmaker.mediateka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import ru.mvrlrd.playlistmaker.databinding.ActivityMediatekaBinding

class MediatekaActivity : AppCompatActivity() {
    private var _binding: ActivityMediatekaBinding? =null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMediatekaBinding.inflate(layoutInflater)
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