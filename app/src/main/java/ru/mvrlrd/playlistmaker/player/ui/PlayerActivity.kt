package ru.mvrlrd.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mvrlrd.playlistmaker.databinding.ActivityPlayerBinding
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.search.data.model.mapTrackToTrackForPlayer
import ru.mvrlrd.playlistmaker.search.util.Debouncer

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









        val bottomSheetBehavior = BottomSheetBehavior.from(binding.BS.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }
        })






        viewModel.playlists.observe(this){
            Log.e("PlayerActivity", "${it.size}")
        }

        binding.backButton.apply {
            setOnClickListener {
                viewModel.onDestroy()
                this@PlayerActivity.finish()
            }
        }
        
        binding.playButton.apply {
            setOnClickListener {
                if(Debouncer().playClickDebounce(this, lifecycleScope)) {
                    viewModel.playbackControl()
                }
            }
        }

        binding.likeButton.apply {
            setOnClickListener {
                if(Debouncer().playClickDebounce(this, lifecycleScope)) {
                    viewModel.handleLikeButton()
                }
            }
        }

        binding.addToPlaylistBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
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

    private fun parseIntent(): PlayerTrack {
        return args.track.mapTrackToTrackForPlayer()
    }


}






