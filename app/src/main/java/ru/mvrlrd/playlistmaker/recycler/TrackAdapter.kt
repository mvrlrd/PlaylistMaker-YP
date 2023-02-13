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
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

private const val TIME_FORMAT = "mm:ss"
class TrackAdapter: RecyclerView.Adapter<TrackViewHolder> () {
    var tracks: MutableList<Track> by Delegates.observable(mutableListOf()){
            _, old, new ->
        notifyDataSetChanged()
        println("$old -> $new")
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { println("$position was pushed") }
    }
    override fun getItemCount(): Int {
        return tracks.size
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
        trackTime.text = SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(track.trackTime.toLong())
        Glide
            .with(itemView)
            .load(track.image)
            .placeholder(R.drawable.ic_free_icon_font_cross)
            .transform(CenterCrop(), RoundedCorners(8))
            .into(albumImage)
    }
}

