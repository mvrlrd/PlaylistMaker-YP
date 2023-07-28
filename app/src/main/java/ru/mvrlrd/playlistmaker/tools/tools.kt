package ru.mvrlrd.playlistmaker.tools

import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun getCommonDurationOfTracks(tracks: List<TrackForAdapter>): Int {
    val duration = tracks.sumOf {
        it.trackTime!!.toLong()
    }
    return SimpleDateFormat(TRACK_DURATION_FORMAT, Locale.getDefault()).format(duration).toInt()
}

 fun ImageView.loadPlaylist(anySource: Any?, size: Int, radius: Int) {
     Glide
         .with(this)
         .load(anySource)
         .placeholder(
             ResourcesCompat.getDrawable(
                 resources,
                 R.drawable.placeholder,
                 context.theme
             )
         )
         .apply(
             RequestOptions().override(
                 size,
                 size
             )
                 .centerCrop()
         ).apply {
             if (radius != 0) {
                 this
                     .transform(
                         CenterCrop(),
                         RoundedCorners(radius)
                     )
             }
         }

         .into(this)

 }



fun ImageView.loadPlaylistImageNEW(playlistImagePath: String?, size: Int = 450, radius: Int = 0) {
    val file = try {
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            resources.getString(R.string.my_album_name)
        )
        File(filePath, playlistImagePath)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    loadPlaylist(file, size, radius)
}

fun ImageView.loadPlaylistImageNEW(uri: Uri?, size: Int = 450, radius: Int = 0) {
    loadPlaylist(uri, size, radius)
}

 fun generateImageNameForStorage(): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..5)
        .map { allowedChars.random() }
        .joinToString("")
        .plus(IMAGE_TYPE)
}

private const val TRACK_DURATION_FORMAT = "mm"
const val IMAGE_TYPE = ".jpg"
