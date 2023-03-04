package ru.mvrlrd.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlayerActivity : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var albumImage: ImageView
    private lateinit var trackNameText: TextView
    private lateinit var singerNameText: TextView
    private lateinit var addButton: Button
    private lateinit var playButton: Button
    private lateinit var likeButton: Button
    private lateinit var duration: TextView
    private lateinit var album: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        backButton = findViewById<ImageButton?>(R.id.backButton).apply {
            setOnClickListener { onBackPressed() }
        }
        albumImage = findViewById(R.id.albumImageView)
        trackNameText = findViewById(R.id.trackName)
        singerNameText = findViewById(R.id.singerName)
        addButton = findViewById<Button?>(R.id.button).apply {
            setOnClickListener {  }
        }
        playButton = findViewById<Button?>(R.id.playButton).apply {
            setOnClickListener {  }
        }
        likeButton = findViewById<Button?>(R.id.likeButton).apply {
            setOnClickListener {  }
        }

        duration = findViewById(R.id.durationParam)
        album = findViewById(R.id.albumParam)
        year = findViewById(R.id.yearParam)
        genre = findViewById(R.id.genreParam)
        country = findViewById(R.id.countryParam)


    }
}