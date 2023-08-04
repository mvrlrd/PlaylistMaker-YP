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
import ru.mvrlrd.playlistmaker.tools.loadImage
import java.text.SimpleDateFormat
import java.util.*

sealed class PlayerScreenState {


    object BeginningScreenState : PlayerScreenState(){
        override fun render(binding: FragmentPlayerBinding) {
            binding.fabPlay.isEnabled = false
            binding.fabPlay.alpha = INACTIVE_PLAY_BUTTON_ALPHA
            binding.fabAddToFavs.isEnabled = false
            binding.fabAddToFavs.alpha = INACTIVE_PLAY_BUTTON_ALPHA
            binding.fabOpenBottomSheet.isEnabled = true
            binding.fabOpenBottomSheet.alpha = INACTIVE_PLAY_BUTTON_ALPHA
        }
    }


    class LoadTrackInfo(private val track: PlayerTrack): PlayerScreenState(){
        override fun render(binding: FragmentPlayerBinding) {
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
            binding.ivAlbumImage.loadImage(
                track.getCoverArtwork(),
                size = binding.ivAlbumImage.resources.getInteger(R.integer.picture_big_size),
                radius = binding.ivAlbumImage.resources.getDimensionPixelSize(R.dimen.radius_medium)
            )
        }
    }

    object EnablePlayButton : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.fabPlay.isEnabled = true
            binding.fabPlay.alpha = ACTIVE_PLAY_BUTTON_ALPHA
        }
    }


    class HandleLikeButton(private val isFavorite: Boolean): PlayerScreenState(){
        override fun render(binding: FragmentPlayerBinding) {
            binding.fabAddToFavs.isEnabled = true
            binding.fabAddToFavs.alpha = ACTIVE_PLAY_BUTTON_ALPHA
            changeLikeButtonAppearance(binding, isFavorite)
        }

        private fun changeLikeButtonAppearance(binding: FragmentPlayerBinding, isFavorite: Boolean) {
            val icon = if (isFavorite) {
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
    }

    object StopPlaying :
        PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
                    binding.fabPlay.setImageResource(R.drawable.baseline_play_arrow_24)
            }
        }
    object StartPlaying :
        PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.fabPlay.setImageResource(R.drawable.baseline_pause_24)
        }
    }





    class RenderTrackTimer(private val progress: String) : PlayerScreenState() {
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



    object PlayerError : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.fabPlay.alpha = INACTIVE_PLAY_BUTTON_ALPHA
            binding.fabAddToFavs.alpha = INACTIVE_PLAY_BUTTON_ALPHA
            binding.fabOpenBottomSheet.alpha = INACTIVE_PLAY_BUTTON_ALPHA

            binding.fabPlay.isEnabled = false
            binding.fabAddToFavs.isEnabled = false
            binding.fabOpenBottomSheet.isEnabled = false

        }
    }


    companion object {
        private const val INACTIVE_PLAY_BUTTON_ALPHA = 0.5f
        private const val ACTIVE_PLAY_BUTTON_ALPHA = 1f
    }
}