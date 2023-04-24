package ru.mvrlrd.playlistmaker.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {
    private lateinit var searchButton: Button
    private lateinit var mediaButton: Button
    private lateinit var settingsButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById<Button>(R.id.search_button).apply {
            setOnClickListener {
                navigateToC(SearchActivity::class.java)
            }
        }
        mediaButton = findViewById<Button>(R.id.mediateka_button).apply {
            setOnClickListener {
                navigateTo(MediatekaActivity::class.java)
            }
        }
        settingsButton = findViewById<Button>(R.id.settings_button).apply {
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