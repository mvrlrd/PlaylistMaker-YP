package ru.mvrlrd.playlistmaker.tools

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.mvrlrd.playlistmaker.R
import java.io.File
import java.util.*

fun addSuffix(count: Int): String {
    val language = Locale.getDefault().language
    return when (count) {
        1 -> {
            ""
        }
        2, 3, 4 -> {
            if (language == "ru") {
                "а"
            } else {
                "s"
            }
        }
        else -> {
            if (language == "ru") {
                "ов"
            } else {
                "s"
            }
        }
    }
}

fun loadPlaylistImageFromFile(imageView: ImageView, file: File, size: Int){
    Glide
        .with(imageView)
        .load(file)
        .centerCrop()
        .placeholder(R.drawable.album_placeholder_image)
        .apply(
            RequestOptions().override(
                size,
                size
            )
        )
        .into(imageView)
}

fun loadPlaylistImageFromStorage(imageView: ImageView, path: String, size: Int){
    Glide
        .with(imageView)
        .load(path)
        .centerCrop()
        .placeholder(R.drawable.album_placeholder_image)
        .apply(
            RequestOptions().override(
                size,
                size
            )
        )
        .into(imageView)
}

