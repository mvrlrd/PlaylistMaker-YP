package ru.mvrlrd.playlistmaker.tools

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.mvrlrd.playlistmaker.R
import java.io.File

class GlideHelper {

    fun loadImage(imageView: ImageView, file: File, size: Int){
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
}