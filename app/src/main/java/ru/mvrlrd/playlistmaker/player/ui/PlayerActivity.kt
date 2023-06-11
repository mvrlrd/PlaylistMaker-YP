package ru.mvrlrd.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mvrlrd.playlistmaker.databinding.ActivityPlayerBinding
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer
import ru.mvrlrd.playlistmaker.search.data.model.mapTrackToTrackForPlayer

class PlayerActivity : AppCompatActivity() {
    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<PlayerActivityArgs>()
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(parseIntent())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.apply {
            setOnClickListener { onBackPressed() }
        }
        binding.playButton.apply {
            setOnClickListener {
                viewModel.playbackControl()
            }
        }
        viewModel.screenState.observe(this) {
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
        return args.track.mapTrackToTrackForPlayer()
    }
}






