package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.PlaylistCardItemBinding
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter



class PlaylistAdapter :
    ListAdapter<PlaylistForAdapter, PlaylistAdapter.PlaylistViewHolder>(PlaylistItemDiffCallback()) {
    var onClickListener: ((PlaylistForAdapter) -> Unit)? = null
    var showImage: ((ImageView, String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val binding = PlaylistCardItemBinding.inflate(layoutInspector, parent, false)
        return PlaylistViewHolder(binding)
    }

    fun isListEmpty(): Boolean {
        return itemCount == 0
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener?.invoke(item)
        }

        showImage?.invoke(holder.ivPlaylist, item.playlistImagePath)

        holder.bind(item)
    }

    class PlaylistViewHolder(private val binding: PlaylistCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivPlaylist: ImageView = binding.ivPlaylistBigImage

        fun bind(playlist: PlaylistForAdapter) {
            binding.tvTitle.text = playlist.name
            binding.tvTracksQuantity.text = binding.tvTracksQuantity.resources.getQuantityString(
                R.plurals.plural_tracks, playlist.tracksQuantity, playlist.tracksQuantity
            )
        }
    }
}