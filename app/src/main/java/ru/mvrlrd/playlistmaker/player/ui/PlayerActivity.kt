package ru.mvrlrd.playlistmaker.player.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mvrlrd.playlistmaker.databinding.ActivityPlayerBinding
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer
import ru.mvrlrd.playlistmaker.search.data.model.mapTrackToTrackForPlayer
import ru.mvrlrd.playlistmaker.search.domain.Track

class PlayerActivity : AppCompatActivity() {

    private val viewModel: PlayerViewModel by viewModel{
        parametersOf(parseIntent())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.apply {
            setOnClickListener { onBackPressed() }
        }
        binding.playButton.apply {
            setOnClickListener {
                viewModel.playbackControl()
            }
        }
        viewModel.screenState.observe(this){
            it.render(binding)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun parseIntent(): TrackForPlayer {
        if (!intent.hasExtra(TRACK_KEY)){
            throw RuntimeException("Track is absent")
        }
        return try {
            val track = intent.getSerializableExtra(TRACK_KEY) as Track
            track.mapTrackToTrackForPlayer()
        }catch (e: Exception){
            throw RuntimeException("Unknown param format in extra")
        }
    }

    companion object{
        private const val TRACK_KEY = "chosen_track"
        fun startPlayerActivity(context: Context, track: Track): Intent {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            return intent
        }
    }
}






