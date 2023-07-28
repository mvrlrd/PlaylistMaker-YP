package ru.mvrlrd.playlistmaker.search.ui


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.player.util.formatTime
import ru.mvrlrd.playlistmaker.databinding.TrackLayoutBinding
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter
import ru.mvrlrd.playlistmaker.tools.loadImage


class TrackAdapter :
    ListAdapter<TrackForAdapter, TrackAdapter.TrackViewHolder>(TrackItemDiffCallback()) {
    var onClickListener: ((TrackForAdapter) -> Unit)? = null
    var onLongClickListener: ((TrackForAdapter) -> Unit)? = null
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
        onLongClickListener?.let {
            holder.itemView.setOnLongClickListener {
                onLongClickListener?.invoke(item)
                true
            }
        }
        holder.bind(item)
    }

    class TrackViewHolder(private val binding: TrackLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(trackForAdapter: TrackForAdapter) {
            binding.tvTrackName.text = trackForAdapter.trackName
            binding.artistName.text = trackForAdapter.artistName
            binding.trackTime.text = trackForAdapter.trackTime?.let { formatTime(it.toInt()) }
            Log.d("TrackAdapter","image for recycler = ${trackForAdapter.getSmallArtwork()}")
            binding.albumImage.loadImage(
                trackForAdapter.getSmallArtwork(),
                size =  binding.albumImage.resources.getInteger(R.integer.picture_small_size),
                radius = binding.albumImage.resources.getDimensionPixelSize(R.dimen.radius_small)
            )

        }
    }
}



