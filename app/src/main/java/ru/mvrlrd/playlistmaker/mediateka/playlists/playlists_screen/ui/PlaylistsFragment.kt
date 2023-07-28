package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentPlaylistsBinding
import ru.mvrlrd.playlistmaker.mediateka.MediatekaFragmentDirections
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter


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
            navigateTaAddPlaylistFragment()
        }
        initRecycler()
        observePlaylistChanges()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.placeHolder.placeholderMessage.text = this.resources.getText(R.string.no_playlists)
    }

    private fun observePlaylistChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playlists.collect() {
                if (it.isNotEmpty()) {
                    it.forEach {playlist ->
                        log(playlist.toString())
                    }
                    binding.placeHolder.infoPlaceHolder.visibility = View.GONE
                } else {
                    binding.placeHolder.infoPlaceHolder.visibility = View.VISIBLE
                }
                playlistAdapter.submitList(it)
            }
        }
    }

    private fun navigateTaAddPlaylistFragment() {
        findNavController().navigate(
            MediatekaFragmentDirections.actionMediatekaFragmentToAddPlaylistFragment()
        )
    }


    private fun initRecycler() {
        playlistAdapter.onClickListener = {playlist ->
            navigateToPlaylistDescriptionFragment(playlist)
        }
        binding.rView.apply {
            adapter = playlistAdapter
            layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
        }
    }

    private fun navigateToPlaylistDescriptionFragment(playlist: PlaylistForAdapter) {
        findNavController().navigate(
            MediatekaFragmentDirections.actionMediatekaFragmentToPlaylistDescriptionFragment(
                playlist
            )
        )
    }

    private fun log(text: String){
        Log.d(TAG, text)
    }

    companion object {
        private const val TAG = "PlaylistsFragment"
        private const val SPAN_COUNT = 2
        const val PLAYLIST_IMAGE_SIZE = 1600
        fun newInstance() = PlaylistsFragment()
    }
}

