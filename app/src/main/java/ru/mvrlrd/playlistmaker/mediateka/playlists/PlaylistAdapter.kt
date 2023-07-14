package ru.mvrlrd.playlistmaker.mediateka.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.PlaylistCardItemBinding
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import java.io.File

class PlaylistAdapter : ListAdapter<PlaylistForAdapter, PlaylistAdapter.PlaylistViewHolder>(PlaylistItemDiffCallback()) {
    var onClickListener: ((PlaylistForAdapter) -> Unit)? = null
    var showImage: ((ImageView)->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val binding = PlaylistCardItemBinding.inflate(layoutInspector, parent, false)
        return PlaylistViewHolder(binding)
    }

    fun isListEmpty(): Boolean{
        return itemCount==0
    }



    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener?.invoke(item)
        }
        showImage?.let { it(holder.albumImage) }
        holder.bind(item)
    }

    class PlaylistViewHolder(private val binding: PlaylistCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val albumImage: ImageView = binding.ivPlaylistImage

        fun bind(playlistForAdapter: PlaylistForAdapter) {

            binding.tvTitle.text = playlistForAdapter.name
            binding.tvTracksQuantity.text = playlistForAdapter.tracksQuantity.toString()



//                Glide
//                    .with(itemView)
//                    .load(playlistForAdapter.playlistImagePath)
//                    .placeholder(R.drawable.album_placeholder_image)
//
////                    .transform(
//////                        CenterCrop(),
//////                        RoundedCorners(binding.ivPlaylistImage.resources.getDimensionPixelSize(R.dimen.radius))
////                    )
//                    .into(binding.ivPlaylistImage)


        }
    }
}