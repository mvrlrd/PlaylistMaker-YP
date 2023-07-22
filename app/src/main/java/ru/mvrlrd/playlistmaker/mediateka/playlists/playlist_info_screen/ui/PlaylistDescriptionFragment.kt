package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentPlaylistDescriptionBinding
import ru.mvrlrd.playlistmaker.tools.loadPlaylist
import java.io.File

import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mvrlrd.playlistmaker.player.ui.PlayerViewModel
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter
import ru.mvrlrd.playlistmaker.tools.getCommonDurationOfTracks
import java.text.SimpleDateFormat
import java.util.*


class PlaylistDescriptionFragment : Fragment() {
    private var _binding: FragmentPlaylistDescriptionBinding? = null
    private val binding: FragmentPlaylistDescriptionBinding get() = _binding!!
    private val args by navArgs<PlaylistDescriptionFragmentArgs>()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    private val viewModel: PlaylistInfoViewModel by viewModel {
        parametersOf(args.playlist.playlistId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDescriptionBinding.inflate(layoutInflater, container, false)

        loadPlaylistImage()

        observeViewModel()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        initBottomSheet()
        return binding.root
    }

    private fun observeViewModel() {
        viewModel.playlist.observe(this) {
            val duration = getCommonDurationOfTracks(it.songs)
            binding.tvPlaylistName.text = it.playlist.name
            binding.tvPlaylistDescription.text = it.playlist.description
            binding.tvPlaylistSize.text = resources.getQuantityString(
                R.plurals.plural_tracks, it.songs.size, it.songs.size
            )
            binding.tvPlaylistDuration.text = resources.getQuantityString(
                R.plurals.plural_minutes, duration, duration
            )
        }
    }

    private fun loadPlaylistImage() {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            resources.getString(R.string.my_album_name)
        )
        val file = File(filePath, args.playlist.playlistImagePath)
        binding.ivPlaylistImage.loadPlaylist(file, 1600)
    }

    private fun initBottomSheet() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.bottomSheetContainerForPlaylist.bottomSheetForPlaylist).apply {
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset+1f
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}