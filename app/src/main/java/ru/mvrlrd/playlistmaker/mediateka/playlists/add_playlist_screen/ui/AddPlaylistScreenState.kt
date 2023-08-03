package ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.ui

import android.content.res.ColorStateList
import android.widget.Button
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentAddPlaylistBinding

sealed class AddPlaylistScreenState {
    class CreateButtonHandler(private val isNullOrEmptyPlaylistName: Boolean): AddPlaylistScreenState(){
        override fun render(binding: FragmentAddPlaylistBinding) {
            if (isNullOrEmptyPlaylistName){
                disableCreateButton(binding.btnCreatePlaylist)
            }else{
                enableCreateButton(binding.btnCreatePlaylist)
            }
        }
        private fun disableCreateButton(button: Button) {
            button.backgroundTintList = ColorStateList.valueOf(
                button.resources.getColor(
                    R.color.bledniyFont, button.context.theme
                )
            )
            button.alpha = HALF_TRANSPARENCY_BUTTON
            button.isEnabled = false
        }

        private fun enableCreateButton(button: Button) {
            button.backgroundTintList = ColorStateList.valueOf(
                button.resources.getColor(
                    R.color.blue, button.context.theme
                )
            )
            button.alpha = NOT_TRANSPARENCY_BUTTON
            button.isEnabled = true
        }
    }


    abstract fun render(binding: FragmentAddPlaylistBinding)

    companion object {
        private const val HALF_TRANSPARENCY_BUTTON = 0.5f
        private const val NOT_TRANSPARENCY_BUTTON = 1f
    }
}