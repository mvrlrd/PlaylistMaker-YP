package ru.mvrlrd.playlistmaker.player.ui

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.ActivityPlayerBinding
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer
import ru.mvrlrd.playlistmaker.unparseDateToYear
import java.text.SimpleDateFormat
import java.util.*

sealed class PlayerScreenState(val track: TrackForPlayer) {
    class Preview(track: TrackForPlayer): PlayerScreenState(track){
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


    abstract fun render(binding: ActivityPlayerBinding)
}