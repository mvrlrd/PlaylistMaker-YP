package ru.mvrlrd.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val onSearchButtonListener = View.OnClickListener {
            Toast.makeText(this@MainActivity, "ПОИСК", Toast.LENGTH_SHORT).show()
        }
        searchButton.setOnClickListener(onSearchButtonListener)

        val mediaButton = findViewById<Button>(R.id.mediateka_button)
        mediaButton.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                "МЕДИАТЕКА",
                Toast.LENGTH_SHORT
            ).show()
        }

        val settingsButton = findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                "НАСТРОЙКИ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}