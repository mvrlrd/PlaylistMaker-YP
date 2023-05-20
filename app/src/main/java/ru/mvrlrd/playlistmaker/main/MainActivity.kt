package ru.mvrlrd.playlistmaker.main


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.mvrlrd.playlistmaker.databinding.ActivityMainBinding
import ru.mvrlrd.playlistmaker.search.ui.SearchActivity
import ru.mvrlrd.playlistmaker.mediateka.MediatekaActivity
import ru.mvrlrd.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.apply {
            setOnClickListener {
                navigateTo(SearchActivity::class.java)
            }
        }
        binding.mediatekaButton.apply {
            setOnClickListener {
                navigateTo(MediatekaActivity::class.java)
            }
        }
        binding.settingsButton.apply {
            setOnClickListener {
                navigateTo(SettingsActivity::class.java)
            }
        }
    }

    private fun navigateTo(clazz: Class<out AppCompatActivity>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }
}