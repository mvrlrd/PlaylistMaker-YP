package ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.navigation.fragment.navArgs
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.mediateka.playlists.PlaylistEditingBaseFragment
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.EditPlaylistFragmentArgs
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.ui.PlaylistsFragment
import ru.mvrlrd.playlistmaker.tools.loadPlaylist
import java.io.File

class EditPlaylistFragment : PlaylistEditingBaseFragment() {
    private val args by navArgs<EditPlaylistFragmentArgs>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitles()
    }

     private fun setTitles() {
        try {
            val filePath = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                resources.getString(R.string.my_album_name)
            )
            val file = File(filePath, args.playlist.playlistImagePath)
            binding.ivNewPlaylistImage.loadPlaylist(file, PlaylistsFragment.PLAYLIST_IMAGE_SIZE)
        } catch (e: Exception) {
            Log.e("PlaylistsFragment", e.message.toString())
        }
        binding.ietPlaylistName.setText(args.playlist.name)
        binding.ietDesctiption.setText(args.playlist.description)
        binding.btnCreatePlaylist.text = "Сохранить"
        binding.btnBack.title = "Редактировать"
    }

    override fun addPlaylist(isImageNotEmpty: Boolean): String {
            val name = binding.ietPlaylistName.text.toString()
            val description = binding.ietDesctiption.text.toString()
            val nameOfImage = if (isImageNotEmpty) {
                viewModel.generateImageNameForStorage()
            } else {
                ""
            }
            viewModel.addPlaylist(
                PlaylistForAdapter(
                    name = name,
                    description = description,
                    playlistImagePath = nameOfImage
                )
            )
            return nameOfImage

    }
}