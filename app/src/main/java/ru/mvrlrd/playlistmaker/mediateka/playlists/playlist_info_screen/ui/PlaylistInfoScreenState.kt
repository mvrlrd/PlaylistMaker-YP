package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.ui

import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentPlaylistDescriptionBinding
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfo
import ru.mvrlrd.playlistmaker.tools.getCommonDurationOfTracks

sealed class PlaylistInfoScreenState{

    class InitialState(private val playlistInfo: PlaylistInfo): PlaylistInfoScreenState(){
        override fun render(binding: FragmentPlaylistDescriptionBinding) {
            val duration = getCommonDurationOfTracks(playlistInfo.songs)
            binding.tvPlaylistName.text = playlistInfo.playlist.name
            binding.tvPlaylistDescription.text = playlistInfo.playlist.description
            binding.tvPlaylistSize.text = binding.tvPlaylistSize.resources.getQuantityString(
                R.plurals.plural_tracks, playlistInfo.songs.size, playlistInfo.songs.size
            )
            binding.tvPlaylistDuration.text = binding.tvPlaylistDuration.resources.getQuantityString(
                R.plurals.plural_minutes, duration, duration
            )
        }
    }



    abstract fun render(binding: FragmentPlaylistDescriptionBinding)
}
