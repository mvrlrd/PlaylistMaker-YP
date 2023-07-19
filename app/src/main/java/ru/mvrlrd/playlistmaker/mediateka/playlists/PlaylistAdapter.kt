package ru.mvrlrd.playlistmaker.mediateka.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.PlaylistCardItemBinding
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.tools.addSuffix

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
        if (item.playlistImagePath.isNotEmpty()) {
            showImage?.let { it(holder.albumImage, item.playlistImagePath) }
        }

        holder.bind(item)
    }

    class PlaylistViewHolder(private val binding: PlaylistCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val albumImage: ImageView = binding.ivPlaylistBigImage

        fun bind(playlistForAdapter: PlaylistForAdapter) {
            binding.tvTitle.text = playlistForAdapter.name
            binding.tvTracksQuantity.text = binding.tvTracksQuantity.resources.getString(
                R.string.quantity_of_tracks, playlistForAdapter.tracksQuantity.toString(),
                addSuffix(playlistForAdapter.tracksQuantity)
            )

                Glide
                    .with(itemView)
                    .load(playlistForAdapter.playlistImagePath)
                    .centerCrop()
                    .placeholder(R.drawable.album_placeholder_image)
                    .apply(RequestOptions().override(160,160))
                    .into(binding.ivPlaylistBigImage)
        }
    }
}