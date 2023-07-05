package ru.mvrlrd.playlistmaker.mediateka.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.TrackLayoutBinding
import ru.mvrlrd.playlistmaker.player.util.formatTime
import ru.mvrlrd.playlistmaker.search.domain.Track
import ru.mvrlrd.playlistmaker.search.ui.TrackItemDiffCallback

class FavoriteAdapter: ListAdapter<Track, FavoriteAdapter.TrackViewHolder>(TrackItemDiffCallback()) {
    var onClickListener: ((Track) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val binding = TrackLayoutBinding.inflate(layoutInspector, parent, false)
        return TrackViewHolder(binding)
    }

    fun isListEmpty(): Boolean {
        return itemCount == 0
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener?.invoke(item)
        }
        holder.bind(item)
    }

    class TrackViewHolder(private val binding: TrackLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            binding.trackName.text = track.trackName
            binding.artistName.text = track.artistName
//        artistName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ellipse_1, 0, 0, 0);
            binding.trackTime.text = track.trackTime?.let { formatTime(it.toInt()) }
            Glide
                .with(itemView)
                .load(track.image)
                .placeholder(R.drawable.album_placeholder_image)
                .transform(
                    CenterCrop(),
                    RoundedCorners(binding.albumImage.resources.getDimensionPixelSize(R.dimen.radius))
                )
                .into(binding.albumImage)
        }
    }
}