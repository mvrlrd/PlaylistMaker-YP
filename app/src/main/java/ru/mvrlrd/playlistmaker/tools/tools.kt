package ru.mvrlrd.playlistmaker.tools

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.mvrlrd.playlistmaker.R
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

fun loadPlaylistImageFromFile(view: ImageView, anySource: Any, size: Int){
    Glide
        .with(view)
        .load(anySource)
        .centerCrop()
        .placeholder(R.drawable.album_placeholder_image)
        .apply(
            RequestOptions().override(
                size,
                size
            )
        )
        .into(view)
}

