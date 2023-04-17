package ru.mvrlrd.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.mvrlrd.playlistmaker.data.model.TrackModel
import java.text.SimpleDateFormat
import java.util.*
import ru.mvrlrd.playlistmaker.PlayerState.*
import ru.mvrlrd.playlistmaker.domain.Track

class PlayerActivity : AppCompatActivity() {
    private var playerState: PlayerState = STATE_DEFAULT
    private lateinit var handler: Handler
    private val timerGo =
        object : Runnable {
            override fun run() {
                refreshTimer()
                handler.postDelayed(
                    this,
                    REFRESH_TIMER_DELAY_MILLIS,
                )
            }
        }

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
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var track: Track


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        mediaPlayer = MediaPlayer()
        handler = Handler(Looper.getMainLooper())
        backButton = findViewById<ImageButton?>(R.id.backButton).apply {
            setOnClickListener { onBackPressed() }
        }
        albumImage = findViewById(R.id.albumImageView)
        trackNameText = findViewById(R.id.trackName)
        singerNameText = findViewById(R.id.singerName)
        addButton = findViewById<FloatingActionButton?>(R.id.button).apply {
            setOnClickListener { }
        }
        playButton = findViewById<FloatingActionButton?>(R.id.playButton).apply {
            setOnClickListener {
                playbackControl()
            }
        }

        likeButton = findViewById<FloatingActionButton?>(R.id.likeButton).apply {
            setOnClickListener { }
        }

        clockText = findViewById(R.id.clockTrack)

        duration = findViewById(R.id.durationParam)
        album = findViewById(R.id.albumParam)
        year = findViewById(R.id.yearParam)
        genre = findViewById(R.id.genreParam)
        country = findViewById(R.id.countryParam)

        track = intent.getSerializableExtra("my_track") as Track

        trackNameText.text = track.trackName
        singerNameText.text = track.artistName
        duration.text = SimpleDateFormat(
            resources.getString(R.string.track_duration_time_format),
            Locale.getDefault()
        ).format(track.trackTime.toLong())
        album.text = track.album
        year.text = unparseDateToYear(track.year)
        genre.text = track.genre
        country.text = track.country

        Glide
            .with(albumImage)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.album_placeholder_image)
            .transform(
                CenterCrop(),
                RoundedCorners(albumImage.resources.getDimensionPixelSize(R.dimen.big_radius))
            )
            .into(albumImage)

        preparePlayer()
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            refreshPlayersSettings()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.baseline_pause_24)
        playerState = STATE_PLAYING
        handler.postDelayed(timerGo, REFRESH_TIMER_DELAY_MILLIS)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.baseline_play_arrow_24)
        playerState = STATE_PAUSED
        handler.removeCallbacks(timerGo)
    }

    private fun refreshPlayersSettings(){
        playButton.setImageResource(R.drawable.baseline_play_arrow_24)
        playerState = STATE_PREPARED
        handler.removeCallbacks(timerGo)
        clockText.text = resources.getText(R.string.null_timer)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
            STATE_DEFAULT -> {

            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun refreshTimer() {
        val time = formatTime(mediaPlayer.currentPosition)
        clockText.text = time
    }

    companion object {
        const val REFRESH_TIMER_DELAY_MILLIS = 300L
    }
}

enum class PlayerState{
    STATE_DEFAULT,
    STATE_PREPARED,
    STATE_PLAYING,
    STATE_PAUSED
}





