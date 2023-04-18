package ru.mvrlrd.playlistmaker.ui.recycler


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.formatTime
import ru.mvrlrd.playlistmaker.data.model.TrackDto
import kotlin.collections.ArrayList


class TrackAdapter(private val onClickListener: TrackClickListener): RecyclerView.Adapter<TrackViewHolder> () {
    val trackDtos: MutableList<TrackDto> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackDtos[position])
        holder.itemView.setOnClickListener { onClickListener.onTrackClick(trackDtos[position]) }
    }
    override fun getItemCount(): Int {
        return trackDtos.size
    }

    fun interface TrackClickListener {
        fun onTrackClick(trackDto: TrackDto)
    }

    fun setTracks(newTrackListModel: ArrayList<TrackDto>?) {
        if (trackDtos.isNotEmpty()) {
            trackDtos.clear()
        }
        newTrackListModel?.let {
            trackDtos.addAll(it)
        }
        notifyDataSetChanged()
    }
}

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val albumImage: ImageView = itemView.findViewById(R.id.albumImage)

    fun bind(trackDto: TrackDto) {
        trackName.text = trackDto.trackName
        artistName.text = trackDto.artistName
//        artistName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ellipse_1, 0, 0, 0);
        trackTime.text = formatTime(trackDto.trackTime.toInt())
        Glide
            .with(itemView)
            .load(trackDto.image)
            .placeholder(R.drawable.album_placeholder_image)
            .transform(CenterCrop(), RoundedCorners(albumImage.resources.getDimensionPixelSize(R.dimen.radius)))
            .into(albumImage)

    }
}

