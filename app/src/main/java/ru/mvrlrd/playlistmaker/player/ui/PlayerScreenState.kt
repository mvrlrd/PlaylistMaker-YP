package ru.mvrlrd.playlistmaker.player.ui

import android.content.res.ColorStateList
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.ActivityPlayerBinding
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.player.util.unparseDateToYear
import java.text.SimpleDateFormat
import java.util.*

sealed class PlayerScreenState {
    class BeginningState(val track: PlayerTrack): PlayerScreenState(){
        override fun render(binding: ActivityPlayerBinding) {
            binding.playButton.alpha = INACTIVE_PLAY_BUTTON_ALPHA
            binding.trackName.text = track.trackName
            binding.singerName.text = track.artistName
            binding.durationParam.text = SimpleDateFormat(
                binding.durationParam.resources.getString(R.string.track_duration_time_format),
                Locale.getDefault()
            ).format(track.trackTime?.toLong() ?: 0L)
            binding.albumParam.text = track.album
            binding.yearParam.text = unparseDateToYear(track.year!!)
            binding.genreParam.text = track.genre
            binding.countryParam.text = track.country
            handleLikeButton(binding, track)


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
    }

    class LikeButtonHandler(val track: PlayerTrack) : PlayerScreenState(){
        override fun render(binding: ActivityPlayerBinding) {
            handleLikeButton(binding, track)
        }
    }
    class PlayButtonHandling(private val playerState: PlayerState) : PlayerScreenState() {
        override fun render(binding: ActivityPlayerBinding) {
            when (playerState) {
                 PlayerState.PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.baseline_pause_24)
                }
                else -> {
                    binding.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
                }
            }
        }
    }

    object Preparing : PlayerScreenState() {
        override fun render(binding: ActivityPlayerBinding) {
            binding.playButton.isEnabled = true
            binding.playButton.alpha = ACTIVE_PLAY_BUTTON_ALPHA
        }
    }
    class Playing(private val progress: String): PlayerScreenState(){
        override fun render(binding: ActivityPlayerBinding) {
            binding.clockTrack.text = progress
        }
    }

    object PlayCompleting : PlayerScreenState() {
        override fun render(binding: ActivityPlayerBinding) {
            binding.clockTrack.text = binding.clockTrack.resources.getText(R.string.null_timer)
            binding.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
        }
    }

    abstract fun render(binding: ActivityPlayerBinding)

     fun handleLikeButton(binding: ActivityPlayerBinding, track: PlayerTrack) {
        val icon = if (track.isFavorite) {
            binding.likeButton.imageTintList = ColorStateList.valueOf(
                binding.likeButton.resources.getColor(
                    R.color.red,
                    binding.likeButton.context.theme
                )
            )
            ResourcesCompat.getDrawable(
                binding.likeButton.resources,
                R.drawable.baseline_favorite_full,
                binding.likeButton.context.theme
            )

        } else {
            binding.likeButton.imageTintList = ColorStateList.valueOf(
                binding.likeButton.resources.getColor(
                    R.color.white,
                    binding.likeButton.context.theme
                )
            )
            ResourcesCompat.getDrawable(
                binding.likeButton.resources,
                R.drawable.baseline_favorite_border_24,
                binding.likeButton.context.theme
            )
        }
        binding.likeButton.setImageDrawable(icon)
    }

    companion object{
        private const val INACTIVE_PLAY_BUTTON_ALPHA = 0.5f
        private const val ACTIVE_PLAY_BUTTON_ALPHA = 1f
    }
}