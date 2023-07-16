package ru.mvrlrd.playlistmaker.mediateka.playlists

import androidx.recyclerview.widget.DiffUtil
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter


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