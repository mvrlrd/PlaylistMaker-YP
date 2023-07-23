package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.ui

import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentPlaylistDescriptionBinding
import ru.mvrlrd.playlistmaker.tools.loadPlaylist
import java.io.File

import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mvrlrd.playlistmaker.search.ui.TrackAdapter
import ru.mvrlrd.playlistmaker.search.util.Debouncer


class PlaylistDescriptionFragment : Fragment() {
    private var _binding: FragmentPlaylistDescriptionBinding? = null
    private val binding: FragmentPlaylistDescriptionBinding get() = _binding!!
    private val args by navArgs<PlaylistDescriptionFragmentArgs>()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val trackAdapter: TrackAdapter by inject()


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

        binding.bottomSheetContainerForPlaylist.rvPlaylists.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
        trackAdapter.apply {
            onClickListener = { track ->
                if (Debouncer().playClickDebounce(scope = lifecycleScope)) {
                    findNavController().navigate(
                        PlaylistDescriptionFragmentDirections.actionPlaylistDescriptionFragmentToPlayerFragment(
                            track)
                    )
                }
            }
        }




        initBottomSheet()
        return binding.root
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.playlistInfo.collect(){
                viewModel.changeState(it)
                trackAdapter.submitList(it.songs)
            }
        }
        viewModel.screenState.observe(this){
            it.render(binding)
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