package ru.mvrlrd.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.PlaylistLayoutBinding
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.ui.PlaylistItemDiffCallback
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.tools.loadPlaylistImageNEW


class PlaylistAdapterForPlayer :
    ListAdapter<PlaylistForAdapter, PlaylistAdapterForPlayer.PlaylistForPlayerViewHolder>(
        PlaylistItemDiffCallback()
    ) {
    var onClickListener: ((PlaylistForAdapter) -> Unit)? = null
    var showImage: ((ImageView, String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistForPlayerViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val binding = PlaylistLayoutBinding.inflate(layoutInspector, parent, false)
        return PlaylistForPlayerViewHolder(binding)
    }

    fun isListEmpty(): Boolean {
        return itemCount == 0
    }

    override fun onBindViewHolder(holder: PlaylistForPlayerViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener?.invoke(item)
        }
        holder.ivPlaylist.loadPlaylistImageNEW(
            item.playlistImagePath,
            size = holder.ivPlaylist.resources.getInteger(R.integer.picture_small_size),
            radius = holder.ivPlaylist.resources.getDimensionPixelSize(R.dimen.radius_small)
        )
        holder.bind(item)
    }

    class PlaylistForPlayerViewHolder(private val binding: PlaylistLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivPlaylist: ImageView = binding.ivPlaylistImage
        fun bind(playlist: PlaylistForAdapter) {
            binding.tvPlaylistName.text = playlist.name
            binding.tvQuantityOfTracks.text = binding.tvQuantityOfTracks.resources.getQuantityString(
                R.plurals.plural_tracks, playlist.tracksQuantity, playlist.tracksQuantity
            )
        }
    }
}






