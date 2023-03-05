package ru.mvrlrd.playlistmaker.recycler


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.mvrlrd.playlistmaker.TrackOnClickListener
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TrackAdapter(private val onClickListener: TrackOnClickListener): RecyclerView.Adapter<TrackViewHolder> () {
    val tracks: MutableList<Track> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { onClickListener.trackOnClick(tracks[position]) }
    }
    override fun getItemCount(): Int {
        return tracks.size
    }

    fun setTracks(newTrackList: ArrayList<Track>?) {
        if (tracks.isNotEmpty()) {
            tracks.clear()
        }
        newTrackList?.let {
            tracks.addAll(it)
        }
        notifyDataSetChanged()
    }
}

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val albumImage: ImageView = itemView.findViewById(R.id.albumImage)

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
//        artistName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ellipse_1, 0, 0, 0);
        trackTime.text = SimpleDateFormat(trackTime.resources.getString(R.string.track_duration_time_format), Locale.getDefault()).format(track.trackTime.toLong())
        Glide
            .with(itemView)
            .load(track.image)
            .placeholder(R.drawable.album_placeholder_image)
            .transform(CenterCrop(), RoundedCorners(albumImage.resources.getDimensionPixelSize(R.dimen.radius)))
            .into(albumImage)

    }
}

