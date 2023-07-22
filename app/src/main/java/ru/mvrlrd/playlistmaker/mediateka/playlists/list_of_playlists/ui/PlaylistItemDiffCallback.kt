package ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.ui

import androidx.recyclerview.widget.DiffUtil
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.domain.PlaylistForAdapter


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