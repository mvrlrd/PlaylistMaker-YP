package ru.mvrlrd.playlistmaker.mediateka.playlists.ui.list_of_playlists_screen

import androidx.recyclerview.widget.DiffUtil
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter


class PlaylistItemDiffCallback : DiffUtil.ItemCallback<PlaylistForAdapter>() {
    override fun areItemsTheSame(
        oldItem: PlaylistForAdapter,
        newItem: PlaylistForAdapter
    ): Boolean {
        return oldItem.playlistId == newItem.playlistId
    }

    override fun areContentsTheSame(
        oldItem: PlaylistForAdapter,
        newItem: PlaylistForAdapter
    ): Boolean {
        return ((oldItem.playlistId == newItem.playlistId) && (oldItem.tracksQuantity == newItem.tracksQuantity))
    }
}