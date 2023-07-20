package ru.mvrlrd.playlistmaker.tools

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.mvrlrd.playlistmaker.R
import java.util.*

fun addSuffix(count: Int): String {
    return when (Locale.getDefault().language) {
        "en" -> {
            makeEnglishSuffix(count)
        }
        else -> {
            makeRussianSuffix(count)
        }
    }
}

private fun makeEnglishSuffix(count: Int): String {
    return when (count) {
        1 -> {
            ""
        }
        else->{
            "s"
        }
    }
}
private fun makeRussianSuffix(count: Int): String {
    return when (count % 10) {
        1 -> {
            if (count%100 == 11) {
                "ов"
            } else {
                ""
            }
        }
        2, 3, 4 -> {
            "а"
        }
        else -> {
            "ов"
       }
   }
}


fun ImageView.loadPlaylist(anySource: Any, size: Int) {
    Glide
        .with(this)
        .load(anySource)
        .centerCrop()
        .placeholder(R.drawable.album_placeholder_image)
        .apply(
            RequestOptions().override(
                size,
                size
            )
        )
        .into(this)
}
//fun loadPlaylistImageFromFile(view: ImageView, anySource: Any, size: Int){
//    Glide
//        .with(view)
//        .load(anySource)
//        .centerCrop()
//        .placeholder(R.drawable.album_placeholder_image)
//        .apply(
//            RequestOptions().override(
//                size,
//                size
//            )
//        )
//        .into(view)
//}

