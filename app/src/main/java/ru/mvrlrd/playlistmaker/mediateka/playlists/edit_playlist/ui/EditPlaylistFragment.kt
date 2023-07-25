package ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.navArgs
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.mediateka.playlists.PlaylistEditingBaseFragment
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.ui.PlaylistsFragment
import ru.mvrlrd.playlistmaker.tools.loadPlaylist
import java.io.File
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : PlaylistEditingBaseFragment() {
    private val args by navArgs<EditPlaylistFragmentArgs>()
    override val viewModel: UpdatePlaylistViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitles()
    }

    private fun setTitles() {
        if (args.playlist.playlistImagePath.isNotEmpty()) {
            try {
                val filePath = File(
                    requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    resources.getString(R.string.my_album_name)
                )
                val file = File(filePath, args.playlist.playlistImagePath)
                binding.ivNewPlaylistImage.loadPlaylist(
                    file,
                    PlaylistsFragment.PLAYLIST_IMAGE_SIZE
                )
            } catch (e: Exception) {
                Log.e("PlaylistsFragment", e.message.toString())
            }
        }else{
            binding.ivNewPlaylistImage.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.album_placeholder_image, requireActivity().theme ))
        }
        binding.ietPlaylistName.setText(args.playlist.name)
        binding.ietDesctiption.setText(args.playlist.description)
        binding.btnCreatePlaylist.text = "Сохранить"
        binding.btnBack.title = "Редактировать"
    }


    override fun createPlaylist(): PlaylistForAdapter{
        val name = binding.ietPlaylistName.text.toString()
        val description = binding.ietDesctiption.text.toString()

        return PlaylistForAdapter(
            playlistId = args.playlist.playlistId,
            name = name,
            description = description,
            playlistImagePath = viewModel.getImagePath(_uri != null, args.playlist.playlistImagePath),
        )
    }
}