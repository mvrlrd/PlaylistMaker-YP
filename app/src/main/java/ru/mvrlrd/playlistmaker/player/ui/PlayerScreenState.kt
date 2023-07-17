package ru.mvrlrd.playlistmaker.player.ui

import android.content.res.ColorStateList
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentPlayerBinding
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.player.util.unparseDateToYear
import java.text.SimpleDateFormat
import java.util.*

sealed class PlayerScreenState {
    class BeginningState(private val track: PlayerTrack) : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.fabPlay.alpha = INACTIVE_PLAY_BUTTON_ALPHA
            binding.tvTrackName.text = track.trackName
            binding.tvSingerName.text = track.artistName
            binding.tvDurationParam.text = SimpleDateFormat(
                binding.tvDurationParam.resources.getString(R.string.track_duration_time_format),
                Locale.getDefault()
            ).format(track.trackTime?.toLong() ?: 0L)
            binding.tvAlbumParam.text = track.album
            binding.tvYearParam.text = unparseDateToYear(track.year)
            binding.tvGenreParam.text = track.genre
            binding.tvCountryParam.text = track.country
            handleLikeButton(binding, track)
            Glide
                .with(binding.ivAlbumImage)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.album_placeholder_image)
                .transform(
                    CenterCrop(),
                    RoundedCorners(binding.ivAlbumImage.resources.getDimensionPixelSize(R.dimen.radius_medium))
                )
                .into(binding.ivAlbumImage)
        }
    }

    class LikeButtonHandler(val track: PlayerTrack) : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            handleLikeButton(binding, track)
        }
    }

    class PlayButtonHandling(private val playerState: MyMediaPlayer.PlayerState) :
        PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            when (playerState) {
                MyMediaPlayer.PlayerState.PLAYING -> {
                    binding.fabPlay.setImageResource(R.drawable.baseline_pause_24)
                }
                else -> {
                    binding.fabPlay.setImageResource(R.drawable.baseline_play_arrow_24)
                }
            }
        }
    }

    object Preparing : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.fabPlay.isEnabled = true
            binding.fabPlay.alpha = ACTIVE_PLAY_BUTTON_ALPHA
        }
    }

    class Playing(private val progress: String) : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.tvTimer.text = progress
        }
    }

    object PlayCompleting : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.tvTimer.text = binding.tvTimer.resources.getText(R.string.null_timer)
            binding.fabPlay.setImageResource(R.drawable.baseline_play_arrow_24)
        }
    }

    abstract fun render(binding: FragmentPlayerBinding)

    fun handleLikeButton(binding: FragmentPlayerBinding, track: PlayerTrack) {
        val icon = if (track.isFavorite) {
            binding.fabAddToFavs.imageTintList = ColorStateList.valueOf(
                binding.fabAddToFavs.resources.getColor(
                    R.color.red,
                    binding.fabAddToFavs.context.theme
                )
            )
            ResourcesCompat.getDrawable(
                binding.fabAddToFavs.resources,
                R.drawable.baseline_favorite_full,
                binding.fabAddToFavs.context.theme
            )

        } else {
            binding.fabAddToFavs.imageTintList = ColorStateList.valueOf(
                binding.fabAddToFavs.resources.getColor(
                    R.color.white,
                    binding.fabAddToFavs.context.theme
                )
            )
            ResourcesCompat.getDrawable(
                binding.fabAddToFavs.resources,
                R.drawable.baseline_favorite_border_24,
                binding.fabAddToFavs.context.theme
            )
        }
        binding.fabAddToFavs.setImageDrawable(icon)
    }

    class PlayerError(private val track: PlayerTrack) : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.fabPlay.alpha = INACTIVE_PLAY_BUTTON_ALPHA
            binding.fabAddToFavs.alpha = INACTIVE_PLAY_BUTTON_ALPHA
            binding.fabOpenBottomSheet.alpha = INACTIVE_PLAY_BUTTON_ALPHA

            binding.fabPlay.isEnabled = false
            binding.fabAddToFavs.isEnabled = false
            binding.fabOpenBottomSheet.isEnabled = false

            binding.fabPlay.alpha = INACTIVE_PLAY_BUTTON_ALPHA
            binding.tvTrackName.text = track.trackName
            binding.tvSingerName.text = track.artistName
            binding.tvDurationParam.text = SimpleDateFormat(
                binding.tvDurationParam.resources.getString(R.string.track_duration_time_format),
                Locale.getDefault()
            ).format(track.trackTime?.toLong() ?: 0L)
            binding.tvAlbumParam.text = track.album
            binding.tvYearParam.text = unparseDateToYear(track.year)
            binding.tvGenreParam.text = track.genre
            binding.tvCountryParam.text = track.country
            handleLikeButton(binding, track)

            Glide
                .with(binding.ivAlbumImage)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.album_placeholder_image)
                .transform(
                    CenterCrop(),
                    RoundedCorners(binding.ivAlbumImage.resources.getDimensionPixelSize(R.dimen.radius_medium))
                )
                .into(binding.ivAlbumImage)
        }
    }

    companion object {
        private const val INACTIVE_PLAY_BUTTON_ALPHA = 0.5f
        private const val ACTIVE_PLAY_BUTTON_ALPHA = 1f
    }
}