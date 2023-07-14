package ru.mvrlrd.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomSheet()
        observeViewModel()
        handleBackButton()
        handlePlayButton()
        handleLikeButton()
        handleAddToPlaylistButton()
    }

    private fun handleAddToPlaylistButton() {
        binding.addToPlaylistBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private fun handleLikeButton() {
        binding.likeButton.apply {
            setOnClickListener {
                if (Debouncer().playClickDebounce(this, lifecycleScope)) {
                    viewModel.handleLikeButton()
                }
            }
        }
    }

    private fun handlePlayButton() {
        binding.playButton.apply {
            setOnClickListener {
                if (Debouncer().playClickDebounce(this, lifecycleScope)) {
                    viewModel.playbackControl()
                }
            }
        }
    }

    private fun handleBackButton() {
        binding.backButton.apply {
            setOnClickListener {
                viewModel.onDestroy()
                this@PlayerActivity.finish()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.playlists.observe(this) {
            Log.e("PlayerActivity", "${it.size}")
        }
        viewModel.screenState.observe(this) {
            it.render(binding)
        }
    }


    private fun initBottomSheet(){
         bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer.bottomSheet).apply {
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






