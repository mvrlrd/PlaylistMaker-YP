package ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.ui

import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.PlaylistEditingBaseFragment



open class AddPlaylistFragment : PlaylistEditingBaseFragment() {
    override val viewModel: AddPlaylistViewModel by viewModel()
}
