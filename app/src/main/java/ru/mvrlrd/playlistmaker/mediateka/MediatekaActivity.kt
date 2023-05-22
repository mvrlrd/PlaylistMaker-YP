package ru.mvrlrd.playlistmaker.mediateka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.ActivityMediatekaBinding

class MediatekaActivity : AppCompatActivity() {
    private var _binding: ActivityMediatekaBinding? =null
    private val binding get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediatekaViewPagerAdapter(
            supportFragmentManager, lifecycle
        )

        tabMediator = TabLayoutMediator(binding.mediatekaTab, binding.viewPager){
            tab, position ->
            when(position){
                0 -> tab.text = this.resources.getText(R.string.favorites)
                1 -> tab.text = this.resources.getText(R.string.playlists)
            }
        }.apply {
            attach()
        }


        binding.mediatekaToolbar.apply {
            setNavigationOnClickListener { onBackPressed() }
        }
    }
}