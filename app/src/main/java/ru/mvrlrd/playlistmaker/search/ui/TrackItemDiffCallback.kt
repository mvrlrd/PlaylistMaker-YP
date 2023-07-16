package ru.mvrlrd.playlistmaker.search.ui

import androidx.recyclerview.widget.DiffUtil
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

class TrackItemDiffCallback: DiffUtil.ItemCallback<TrackForAdapter>() {
    override fun areItemsTheSame(oldItem: TrackForAdapter, newItem: TrackForAdapter): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: TrackForAdapter, newItem: TrackForAdapter): Boolean {
        return ((oldItem.trackId == newItem.trackId)&&(oldItem.isFavorite==newItem.isFavorite))
    }
}