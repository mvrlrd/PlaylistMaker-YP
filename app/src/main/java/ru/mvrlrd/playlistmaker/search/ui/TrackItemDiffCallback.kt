package ru.mvrlrd.playlistmaker.search.ui

import androidx.recyclerview.widget.DiffUtil
import ru.mvrlrd.playlistmaker.search.domain.AdapterTrack

class TrackItemDiffCallback: DiffUtil.ItemCallback<AdapterTrack>() {
    override fun areItemsTheSame(oldItem: AdapterTrack, newItem: AdapterTrack): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: AdapterTrack, newItem: AdapterTrack): Boolean {
        return ((oldItem.trackId == newItem.trackId)&&(oldItem.isFavorite==newItem.isFavorite))
    }
}