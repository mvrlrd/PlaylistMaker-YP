package ru.mvrlrd.playlistmaker.player.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.ActivityPlayerBinding
import ru.mvrlrd.playlistmaker.search.data.model.mapTrackToTrackForPlayer
import ru.mvrlrd.playlistmaker.search.domain.Track

class PlayerActivity : ComponentActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val track = intent.getSerializableExtra("my_track") as Track
        viewModel = ViewModelProvider(this, PlayerViewModel.getViewModelFactory(trackForPlayer = track.mapTrackToTrackForPlayer()))[PlayerViewModel::class.java]


        binding.backButton.apply {
            setOnClickListener { onBackPressed() }
        }
        binding.button.apply {
            setOnClickListener {
            }
        }
        binding.playButton.apply {
            setOnClickListener {
                viewModel.playbackControl()
            }
        }
        binding.likeButton.apply {
            setOnClickListener { }
        }


//        viewModel.d()
//        handler.postDelayed(timerGo, REFRESH_TIMER_DELAY_MILLIS)

        viewModel.screenState.observe(this){
            it.render(binding)
        }
    }

    override fun onPause() {
        super.onPause()
//        playerPresenter.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
//        playerPresenter.onDestroy()
        viewModel.onDestroy()
    }



//    override fun onCompletePlaying() {
//        viewModel.onDestroy()
////            handler.removeCallbacks(timerGo)
//            binding.clockTrack.text = resources.getText(R.string.null_timer)
//    }


//    override fun removePostDelay() {
//        viewModel.onDestroy()
////        handler.removeCallbacks(timerGo)
//    }



//    override fun startPostDelay() {
//        viewModel.d()
////        handler.postDelayed(timerGo, REFRESH_TIMER_DELAY_MILLIS)
//    }
//
//    override fun enablePlayButton() {
//        binding.playButton.isEnabled = true
//    }

//    override fun updateTimer(time: String) {
//        binding.clockTrack.text = time
//    }


    companion object {
        const val REFRESH_TIMER_DELAY_MILLIS = 300L
    }
}






