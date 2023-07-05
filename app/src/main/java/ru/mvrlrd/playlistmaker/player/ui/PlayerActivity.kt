package ru.mvrlrd.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mvrlrd.playlistmaker.databinding.ActivityPlayerBinding
import ru.mvrlrd.playlistmaker.mediateka.favorites.FavoritesFragment
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer
import ru.mvrlrd.playlistmaker.search.data.model.mapTrackToTrackForPlayer

class PlayerActivity : AppCompatActivity() {
    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<PlayerActivityArgs>()
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(parseIntent())
    }
    private var isPlayClickAllowed = true
    private var isLikeClickAllowed = true
    private fun playClickDebounce(): Boolean {
        val current = isPlayClickAllowed
        if (isPlayClickAllowed) {
            isPlayClickAllowed = false
            binding.playButton.isEnabled = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isPlayClickAllowed = true
                binding.playButton.isEnabled = true
            }
        }
        return current
    }
    private fun likeClickDebounce(): Boolean {
        val current = isLikeClickAllowed
        if (isLikeClickAllowed) {
            isLikeClickAllowed = false
            binding.likeButton.isEnabled = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isLikeClickAllowed = true
                binding.likeButton.isEnabled = true
            }
        }
        return current
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.apply {
            setOnClickListener {
                //TODO сделать возврат назад из активити с помощью jetpack navigation
                 onBackPressed()
            }
        }
        binding.playButton.apply {
            setOnClickListener {
                if(playClickDebounce()) {
                    viewModel.playbackControl()
                }
            }
        }
        binding.likeButton.apply {
            setOnClickListener {
                if(likeClickDebounce()) {
                    viewModel.handleLikeButton()
                }
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

    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 125L
    }
}






