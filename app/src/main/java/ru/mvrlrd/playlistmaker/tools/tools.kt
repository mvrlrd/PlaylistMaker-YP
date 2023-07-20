package ru.mvrlrd.playlistmaker.tools

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.mvrlrd.playlistmaker.R



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

