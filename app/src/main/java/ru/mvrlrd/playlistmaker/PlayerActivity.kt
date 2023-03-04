package ru.mvrlrd.playlistmaker

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.mvrlrd.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.*

private const val TIME_FORMAT = "mm:ss"
class PlayerActivity : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var albumImage: ImageView
    private lateinit var trackNameText: TextView
    private lateinit var singerNameText: TextView
    private lateinit var addButton: FloatingActionButton
    private lateinit var playButton: FloatingActionButton
    private lateinit var likeButton: FloatingActionButton
    private lateinit var clockText: TextView
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
        addButton = findViewById<FloatingActionButton?>(R.id.button).apply {
            setOnClickListener {  }
        }
        playButton = findViewById<FloatingActionButton?>(R.id.playButton).apply {
            setOnClickListener {  }
        }
        likeButton = findViewById<FloatingActionButton?>(R.id.likeButton).apply {
            setOnClickListener {  }
        }

        clockText = findViewById(R.id.clockTrack)

        duration = findViewById(R.id.durationParam)
        album = findViewById(R.id.albumParam)
        year = findViewById(R.id.yearParam)
        genre = findViewById(R.id.genreParam)
        country = findViewById(R.id.countryParam)

        val track = intent.getSerializableExtra("my_track") as Track

        trackNameText.text = track.trackName
        singerNameText.text = track.artistName
        duration.text = SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(track.trackTime.toLong())
        album.text = track.album
        year.text = unparseDateToYear(track.year)
        genre.text = track.genre
        country.text = track.country

        Glide
            .with(albumImage)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.album_placeholder_image)
            .transform(CenterCrop(), RoundedCorners(albumImage.resources.getDimensionPixelSize(R.dimen.big_radius)))
            .into(albumImage)
    }
}