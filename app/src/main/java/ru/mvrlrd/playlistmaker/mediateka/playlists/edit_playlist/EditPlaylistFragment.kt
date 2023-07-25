package ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentAddPlaylistBinding
import ru.mvrlrd.playlistmaker.mediateka.playlists.PlaylistEditingBaseFragment
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.ui.AddPlaylistFragment
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.ui.AddPlaylistViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.ui.PlaylistsFragment
import ru.mvrlrd.playlistmaker.tools.loadPlaylist
import java.io.File

class EditPlaylistFragment : PlaylistEditingBaseFragment() {
    private val args by navArgs<EditPlaylistFragmentArgs>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitles()
    }

    override fun setTitles() {
        super.setTitles()
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

}