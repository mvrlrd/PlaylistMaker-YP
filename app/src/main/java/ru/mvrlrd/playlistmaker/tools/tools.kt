package ru.mvrlrd.playlistmaker.tools

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

fun getCommonDurationOfTracks(tracks: List<TrackForAdapter>): Int {
    val duration = tracks.sumOf {
        it.trackTime!!.toLong()
    }
    return SimpleDateFormat(TRACK_DURATION_FORMAT, Locale.getDefault()).format(duration).toInt()
}

fun ImageView.loadImage(anySource: Any?, size: Int, radius: Int) {

    Glide
        .with(this)
        .load(anySource)
        .error(ResourcesCompat.getDrawable(
            resources,
            R.drawable.placeholder,
            context.theme
        ))
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

fun Uri.saveImageToInternalStorage(imageView: ImageView, fileToSave: File) {
    val inputStream = imageView.context.contentResolver.openInputStream(this)
    val outputStream = FileOutputStream(fileToSave)
    BitmapFactory
        .decodeStream(inputStream)
        .compress(
            Bitmap.CompressFormat.JPEG,
            IMAGE_QUALITY, outputStream
        )
    Log.d(TAG, "image file saved to storage ${fileToSave.toURI()}")
}

fun String.formatUriToImageName(): String{
    val str = this.replace(":","")
    return "$str.jpg"
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
private const val TAG = "TOOLS"
private const val IMAGE_QUALITY = 30
