package ru.mvrlrd.playlistmaker.mediateka.playlists

import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.mediateka.MediatekaFragmentDirections
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
                MediatekaFragmentDirections.actionMediatekaFragmentToAddPlaylistFragment()
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
        playlistAdapter.onClickListener = {}
        playlistAdapter.showImage = { view, playlistImage ->
            val filePath = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                ALBUM_NAME
            )
            val file = File(filePath, playlistImage)
            view.setImageURI(file.toUri())
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
        fun newInstance() = PlaylistsFragment()
        private const val ALBUM_NAME = "myalbum"
    }
}