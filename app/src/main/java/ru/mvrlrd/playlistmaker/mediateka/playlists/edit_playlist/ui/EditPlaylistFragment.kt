package ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import ru.mvrlrd.playlistmaker.mediateka.playlists.PlaylistEditingBaseFragment
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.tools.loadImage


class EditPlaylistFragment : PlaylistEditingBaseFragment() {
    private val args by navArgs<EditPlaylistFragmentArgs>()
    override val viewModel: UpdatePlaylistViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitles()
    }

    private fun setTitles() {
        val file = viewModel.getFile(args.playlist.playlistImagePath, resources.getString(R.string.my_album_name))
        binding.ivNewPlaylistImage.loadImage(file, size = 1600, radius = resources.getDimensionPixelSize(R.dimen.radius_big))
//        binding.ivNewPlaylistImage.loadPlaylistImageNEW(args.playlist.playlistImagePath, size = 1600, radius = resources.getDimensionPixelSize(R.dimen.radius_big))
        binding.ietPlaylistName.setText(args.playlist.name)
        binding.ietDesctiption.setText(args.playlist.description)
        binding.btnCreatePlaylist.text = resources.getText(R.string.edit_playlist_button)
        binding.btnBack.title = resources.getText(R.string.edit_playlist_title)
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