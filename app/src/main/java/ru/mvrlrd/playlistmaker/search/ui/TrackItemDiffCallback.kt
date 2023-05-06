package ru.mvrlrd.playlistmaker.search.ui

import androidx.recyclerview.widget.DiffUtil
import ru.mvrlrd.playlistmaker.search.domain.Track

class TrackItemDiffCallback: DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }
}