package ru.mvrlrd.playlistmaker.player.ui

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentPlayerBinding
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.domain.AddingTrackToPlaylistResult
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.player.util.unparseDateToYear
import ru.mvrlrd.playlistmaker.tools.loadImage
import java.text.SimpleDateFormat
import java.util.*

sealed class PlayerScreenState {
    abstract fun render(binding: FragmentPlayerBinding)



    class LoadPlaylists(playlists: List<PlaylistForAdapter>): PlayerScreenState(){
        override fun render(binding: FragmentPlayerBinding) {

        }
    }


    object EnablePlayButton : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.fabPlay.isEnabled = true
            binding.fabPlay.alpha = ACTIVE_PLAY_BUTTON_ALPHA
        }
    }

    object EnableAddToPlaylistBtn : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.fabOpenBottomSheet.isEnabled = true
            binding.fabOpenBottomSheet.alpha = ACTIVE_PLAY_BUTTON_ALPHA
        }
    }

    object EnableLikeButton : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.fabAddToFavs.isEnabled = true
            binding.fabAddToFavs.alpha = ACTIVE_PLAY_BUTTON_ALPHA
        }
    }


    class HandleLikeButton(private val isFavorite: Boolean): PlayerScreenState(){
        override fun render(binding: FragmentPlayerBinding) {
            binding.fabAddToFavs.isEnabled = true
            binding.fabAddToFavs.alpha = ACTIVE_PLAY_BUTTON_ALPHA
            changeLikeButtonAppearance(binding)
        }

        private fun changeLikeButtonAppearance(binding: FragmentPlayerBinding) {
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
            Log.d("TAG", "render: StartPlaying")
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
    class AddTrackToPlaylist(private val result: AddingTrackToPlaylistResult): PlayerScreenState(){
        override fun render(binding: FragmentPlayerBinding) {

        }

        fun makeToast(context: Context):Boolean{
            val isTrackInPlaylist = result.wasAdded
            val playlistName = result.playlistName
            val message = if (isTrackInPlaylist) {

                context.resources.getString(R.string.track_added_to_playlist, playlistName)
            } else {
                context.resources.getString(R.string.track_already_added, playlistName)
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            return isTrackInPlaylist
        }
    }

    class UpdatePlaylistList(private val playlists: List<PlaylistForAdapter>): PlayerScreenState(){
        override fun render(binding: FragmentPlayerBinding) {
        }
        fun update(adapter: PlaylistAdapterForPlayer){
            adapter.submitList(playlists)
        }
    }

    class PlayerError : PlayerScreenState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.fabPlay.alpha = INACTIVE_PLAY_BUTTON_ALPHA
            binding.fabAddToFavs.alpha = INACTIVE_PLAY_BUTTON_ALPHA
            binding.fabOpenBottomSheet.alpha = INACTIVE_PLAY_BUTTON_ALPHA

            binding.fabPlay.isEnabled = false
            binding.fabAddToFavs.isEnabled = false
            binding.fabOpenBottomSheet.isEnabled = false

        }
    }

    class LoadTrackInfo(private val track: PlayerTrack): PlayerScreenState() {
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
    object DisableFabs: PlayerScreenState(){
        override fun render(binding: FragmentPlayerBinding) {
            binding.fabPlay.isEnabled = false
            binding.fabPlay.alpha = INACTIVE_PLAY_BUTTON_ALPHA
        }
    }

    companion object {
        private const val INACTIVE_PLAY_BUTTON_ALPHA = 0.5f
        private const val ACTIVE_PLAY_BUTTON_ALPHA = 1f
    }
}