package ru.mvrlrd.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.*
import ru.mvrlrd.playlistmaker.presenter.PlayerPresenter
import ru.mvrlrd.playlistmaker.presenter.PlayerState
import ru.mvrlrd.playlistmaker.search.domain.Track
import ru.mvrlrd.playlistmaker.unparseDateToYear

class PlayerActivity : ComponentActivity(), PlayerView {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel

//    private lateinit var handler: Handler
//    private val timerGo =
//        object : Runnable {
//            override fun run() {
//                updateTimer(playerPresenter.getCurrentPosition())
//                handler.postDelayed(
//                    this,
//                    REFRESH_TIMER_DELAY_MILLIS,
//                )
//            }
//        }


    private lateinit var track: Track
    private lateinit var playerPresenter : PlayerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[PlayerViewModel::class.java]

//        handler = Handler(Looper.getMainLooper())

        binding.backButton.apply {
            setOnClickListener { onBackPressed() }

        }


        binding.button.apply {
            setOnClickListener {

            }
        }
        binding.playButton.apply {
            setOnClickListener {
               playerPresenter.playbackControl()
            }
        }

        binding.likeButton.apply {
            setOnClickListener { }
        }



        track = intent.getSerializableExtra("my_track") as Track

        playerPresenter = PlayerPresenter(track, this as PlayerView)

        viewModel.d()
//        handler.postDelayed(timerGo, REFRESH_TIMER_DELAY_MILLIS)

        binding.trackName.text = track.trackName
        binding.singerName.text = track.artistName
        binding.durationParam.text = SimpleDateFormat(
            resources.getString(R.string.track_duration_time_format),
            Locale.getDefault()
        ).format(track.trackTime?.toLong() ?: 0L)
        binding.albumParam.text = track.album
        binding.yearParam.text = unparseDateToYear(track.year!!)
        binding.genreParam.text = track.genre
        binding.countryParam.text = track.country

        Glide
            .with(binding.albumImageView)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.album_placeholder_image)
            .transform(
                CenterCrop(),
                RoundedCorners(binding.albumImageView.resources.getDimensionPixelSize(R.dimen.big_radius))
            )
            .into(binding.albumImageView)
    }

    override fun onPause() {
        super.onPause()
        playerPresenter.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerPresenter.onDestroy()
        viewModel.onDestroy()
    }

    override fun handlePlayButton(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PLAYING -> {
                binding.playButton.setImageResource(R.drawable.baseline_pause_24)
            }
            else -> {
                binding.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
            }
        }
    }

    override fun onCompletePlaying() {
        viewModel.onDestroy()
//            handler.removeCallbacks(timerGo)
            binding.clockTrack.text = resources.getText(R.string.null_timer)
    }


    override fun removePostDelay() {
        viewModel.onDestroy()
//        handler.removeCallbacks(timerGo)
    }



    override fun startPostDelay() {
        viewModel.d()
//        handler.postDelayed(timerGo, REFRESH_TIMER_DELAY_MILLIS)
    }

    override fun enablePlayButton() {
        binding.playButton.isEnabled = true
    }

//    override fun updateTimer(time: String) {
//        binding.clockTrack.text = time
//    }


    companion object {
        const val REFRESH_TIMER_DELAY_MILLIS = 300L
    }
}






