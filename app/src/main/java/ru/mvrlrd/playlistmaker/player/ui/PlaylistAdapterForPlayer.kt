package ru.mvrlrd.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.PlaylistLayoutBinding
import ru.mvrlrd.playlistmaker.mediateka.playlists.PlaylistItemDiffCallback
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter


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
        if (item.playlistImagePath.isNotEmpty()) {
            showImage?.let { it(holder.playlistImage, item.playlistImagePath) }
        }
        holder.bind(item)
    }

    class PlaylistForPlayerViewHolder(private val binding: PlaylistLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val playlistImage: ImageView = binding.ivPlaylistImage
        fun bind(playlist: PlaylistForAdapter) {
            binding.tvPlaylistName.text = playlist.name

            binding.tvQuantityOfTracks.text = binding.tvQuantityOfTracks.resources.getString(
                R.string.quantity_of_tracks, playlist.tracksQuantity.toString(),
                tr(playlist.tracksQuantity)
            )

            if (playlist.playlistImagePath.isEmpty()) {
                Glide
                    .with(itemView)
                    .load(playlist.playlistImagePath)
                    .placeholder(R.drawable.album_placeholder_image)
                    .into(binding.ivPlaylistImage)
            }
        }
    }
}

private fun tr(count: Int): String{
   val suff = when(count){
        1->{""}
       2,3,4->{"а"}
       else ->{"ов"}
    }
    return suff
}


