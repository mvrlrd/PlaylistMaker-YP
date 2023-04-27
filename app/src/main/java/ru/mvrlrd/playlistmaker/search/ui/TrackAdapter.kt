package ru.mvrlrd.playlistmaker.search.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.search.data.player.util.formatTime
import ru.mvrlrd.playlistmaker.databinding.TrackLayoutBinding
import ru.mvrlrd.playlistmaker.search.domain.Track


class TrackAdapter(private val onClickListener: TrackClickListener): RecyclerView.Adapter<TrackViewHolder> () {
    private val tracks: MutableList<Track> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val binding = TrackLayoutBinding.inflate(layoutInspector, parent, false)
        return TrackViewHolder(binding)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { onClickListener.onTrackClick(tracks[position]) }
    }
    override fun getItemCount(): Int {
        return tracks.size
    }

    fun interface TrackClickListener {
        fun onTrackClick(tracks: Track)
    }

    fun setTracks(newTrackList: List<Track>?) {
        if (tracks.isNotEmpty()) {
            tracks.clear()
        }
        newTrackList?.let {
            tracks.addAll(it)
        }
        notifyDataSetChanged()
    }
}

class TrackViewHolder(private val binding: TrackLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
//        artistName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ellipse_1, 0, 0, 0);
        binding.trackTime.text = track.trackTime?.let { formatTime(it.toInt()) }
        Glide
            .with(itemView)
            .load(track.image)
            .placeholder(R.drawable.album_placeholder_image)
            .transform(CenterCrop(), RoundedCorners(binding.albumImage.resources.getDimensionPixelSize(R.dimen.radius)))
            .into(binding.albumImage)

    }
}

