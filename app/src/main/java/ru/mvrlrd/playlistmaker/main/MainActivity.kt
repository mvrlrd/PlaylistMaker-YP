package ru.mvrlrd.playlistmaker.main


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import ru.mvrlrd.playlistmaker.databinding.ActivityMainBinding
import ru.mvrlrd.playlistmaker.search.ui.SearchActivity
import ru.mvrlrd.playlistmaker.mediateka.MediatekaActivity
import ru.mvrlrd.playlistmaker.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.apply {
            setOnClickListener {
                navigateToC(SearchActivity::class.java)
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
    private fun navigateToC(clazz: Class<out ComponentActivity>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }
}