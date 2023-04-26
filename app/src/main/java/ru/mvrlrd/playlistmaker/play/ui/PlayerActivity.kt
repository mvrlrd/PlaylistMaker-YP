package ru.mvrlrd.playlistmaker.play.ui

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
import ru.mvrlrd.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.*
import ru.mvrlrd.playlistmaker.presenter.PlayerPresenter
import ru.mvrlrd.playlistmaker.presenter.PlayerState
import ru.mvrlrd.playlistmaker.search.domain.Track
import ru.mvrlrd.playlistmaker.unparseDateToYear

class PlayerActivity : AppCompatActivity(), PlayerView {
    private lateinit var handler: Handler
    private val timerGo =
        object : Runnable {
            override fun run() {
                updateTimer(playerPresenter.getCurrentPosition())
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
    private lateinit var track: Track
    private lateinit var playerPresenter : PlayerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

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
               playerPresenter.playbackControl()
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

        playerPresenter = PlayerPresenter(track, this as PlayerView)
        handler.postDelayed(timerGo, REFRESH_TIMER_DELAY_MILLIS)

        trackNameText.text = track.trackName
        singerNameText.text = track.artistName
        duration.text = SimpleDateFormat(
            resources.getString(R.string.track_duration_time_format),
            Locale.getDefault()
        ).format(track.trackTime?.toLong() ?: 0L)
        album.text = track.album
        year.text = unparseDateToYear(track.year!!)
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
    }

    override fun onPause() {
        super.onPause()
        playerPresenter.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerPresenter.onDestroy()
    }

    override fun handlePlayButton(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PLAYING -> {
                playButton.setImageResource(R.drawable.baseline_pause_24)
            }
            else -> {
                playButton.setImageResource(R.drawable.baseline_play_arrow_24)
            }
        }
    }

    override fun onCompletePlaying() {
            handler.removeCallbacks(timerGo)
            clockText.text = resources.getText(R.string.null_timer)
    }


    override fun removePostDelay() {
        handler.removeCallbacks(timerGo)
    }



    override fun startPostDelay() {
        handler.postDelayed(timerGo, REFRESH_TIMER_DELAY_MILLIS)
    }

    override fun enablePlayButton() {
        playButton.isEnabled = true
    }

    override fun updateTimer(time: String) {
        clockText.text = time
    }


    companion object {
        const val REFRESH_TIMER_DELAY_MILLIS = 300L
    }
}






