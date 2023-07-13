package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentAddPlaylistBinding
import ru.mvrlrd.playlistmaker.databinding.FragmentPlaylistsBinding


class AddPlaylistFragment : Fragment() {
    private var _binding: FragmentAddPlaylistBinding? =null
    private val binding get() = _binding?: throw RuntimeException("FragmentAddPlaylistBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddPlaylistFragment()
    }
}