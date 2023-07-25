package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentPlaylistsBinding
import ru.mvrlrd.playlistmaker.mediateka.MediatekaFragmentDirections
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.tools.loadPlaylist
import java.io.File

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistsViewModel by viewModel()
    private val playlistAdapter: PlaylistAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        binding.addPlaylistButton.setOnClickListener {
            findNavController().navigate(
                MediatekaFragmentDirections.actionMediatekaFragmentToAddPlaylistFragment(PlaylistForAdapter(-1L,"","","",0))
            )
        }

        initRecycler()

        viewModel.playlists.observe(this) {
            if (it.isNotEmpty()) {
                binding.placeHolder.infoPlaceHolder.visibility = View.GONE
            } else {
                binding.placeHolder.infoPlaceHolder.visibility = View.VISIBLE
            }
            playlistAdapter.submitList(it)
        }
        return binding.root
    }

    private fun initRecycler() {
        playlistAdapter.onClickListener = {playlist ->
            findNavController().navigate(
           MediatekaFragmentDirections.actionMediatekaFragmentToPlaylistDescriptionFragment(playlist)
            )
        }
        playlistAdapter.showImage = { view, playlistImagePath ->
            try {
                val filePath = File(
                    requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    resources.getString(R.string.my_album_name)
                )
                val file = File(filePath, playlistImagePath)
                view.loadPlaylist(anySource = file, size = PLAYLIST_IMAGE_SIZE)
            } catch (e: Exception) {
                Log.e("PlaylistsFragment", e.message.toString())
            }
        }
        binding.rView.apply {
            adapter = playlistAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.placeHolder.placeholderMessage.text = this.resources.getText(R.string.no_playlists)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val PLAYLIST_IMAGE_SIZE = 1600
        fun newInstance() = PlaylistsFragment()
    }
}

