package ru.mvrlrd.playlistmaker.player.ui

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.ActivityPlayerBinding
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer
import ru.mvrlrd.playlistmaker.player.util.unparseDateToYear
import java.text.SimpleDateFormat
import java.util.*

sealed class PlayerScreenState {
    class BeginningState(val track: TrackForPlayer): PlayerScreenState(){
        override fun render(binding: ActivityPlayerBinding) {
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

    class PlayButtonHandling(private val playerState: PlayerState) : PlayerScreenState() {
        override fun render(binding: ActivityPlayerBinding) {
            when (playerState) {
                PlayerState.STATE_PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.baseline_pause_24)
                }
                else -> {
                    binding.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
                }
            }
        }
    }

    class Preparing: PlayerScreenState(){
        override fun render(binding: ActivityPlayerBinding) {
            binding.playButton.isEnabled = true
        }
    }
    class TimerUpdating(private val time: String): PlayerScreenState(){
        override fun render(binding: ActivityPlayerBinding) {
            binding.clockTrack.text = time
        }
    }
    class PlayCompleting: PlayerScreenState(){
        override fun render(binding: ActivityPlayerBinding) {
            binding.clockTrack.text = binding.clockTrack.resources.getText(R.string.null_timer)
            binding.playButton.setImageResource(R.drawable.baseline_play_arrow_24)
        }
    }

    abstract fun render(binding: ActivityPlayerBinding)
}