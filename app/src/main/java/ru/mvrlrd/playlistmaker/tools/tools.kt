package ru.mvrlrd.playlistmaker.tools

import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.mediateka.playlists.HandlePlaylistBaseViewModel
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun getCommonDurationOfTracks(tracks: List<TrackForAdapter>):Int{
    val duration = tracks.sumOf {
        it.trackTime!!.toLong()
    }
    return SimpleDateFormat("mm", Locale.getDefault()).format(duration).toInt()
}

fun ImageView.loadPlaylist(anySource: Any, size: Int) {
    Glide
        .with(this)
        .load(anySource)
        .error(R.drawable.album_placeholder_image)
        .placeholder(ResourcesCompat.getDrawable(resources, R.drawable.baseline_pause_24, context.theme))
//        .placeholder(R.drawable.album_placeholder_image)
        .fitCenter()
        .centerCrop()
        .apply(
            RequestOptions().override(
                size,
                size
            )
        )
        .into(this)

}

 fun generateImageNameForStorage(): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..5)
        .map { allowedChars.random() }
        .joinToString("")
        .plus(IMAGE_TYPE)
}


const val IMAGE_TYPE = ".jpg"
