package ru.mvrlrd.playlistmaker.mediateka.playlists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? =null
    private val binding get() = _binding!!
    private val viewModel: PlaylistsViewModel by viewModel()

    companion object {
        fun newInstance() = PlaylistsFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.placeHolder.placeholderMessage.text = this.resources.getText(R.string.no_playlists)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}