package ru.mvrlrd.playlistmaker.search.data.player.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import ru.mvrlrd.playlistmaker.databinding.ActivityPlayerBinding
import ru.mvrlrd.playlistmaker.search.data.model.mapTrackToTrackForPlayer
import ru.mvrlrd.playlistmaker.search.domain.Track

class PlayerActivity : ComponentActivity() {
    private lateinit var viewModel: PlayerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val track = intent.getSerializableExtra("my_track") as Track
        viewModel = ViewModelProvider(this,
            PlayerViewModel.getViewModelFactory(trackForPlayer = track.mapTrackToTrackForPlayer())
        )[PlayerViewModel::class.java]
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
}






