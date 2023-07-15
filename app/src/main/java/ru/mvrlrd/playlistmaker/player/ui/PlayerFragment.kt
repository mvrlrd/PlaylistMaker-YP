package ru.mvrlrd.playlistmaker.player.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mvrlrd.playlistmaker.databinding.FragmentPlayerBinding
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.search.data.model.mapTrackToTrackForPlayer
import ru.mvrlrd.playlistmaker.search.domain.AdapterTrack
import ru.mvrlrd.playlistmaker.search.util.Debouncer
import java.io.File


class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val playlistAdapter: PlaylistAdapterForPlayer by inject()

    private val args by navArgs<PlayerFragmentArgs>()
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(parseIntent(args.adapterTrack))
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(layoutInflater, container, false)
        initBottomSheet()
        observeViewModel()
        handleBackButton()
        handlePlayButton()
        handleLikeButton()
        handleAddToPlaylistButton()
        handleAddToPlaylistButton1()

        initRecycler()
        return binding.root
    }

    private fun handleAddToPlaylistButton() {
        binding.addToPlaylistBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private fun handleAddToPlaylistButton1(){
        binding.bottomSheetContainer.btAddNewPlaylist.setOnClickListener {
            findNavController().navigate(
                PlayerFragmentDirections.actionPlayerFragmentToAddPlaylistFragment()
            )
        }
    }

    private fun handleLikeButton() {
        binding.likeButton.apply {
            setOnClickListener {
                if (Debouncer().playClickDebounce(this, lifecycleScope)) {
                    viewModel.handleLikeButton()
                }
            }
        }
    }

    private fun handlePlayButton() {
        binding.playButton.apply {
            setOnClickListener {
                if (Debouncer().playClickDebounce(this, lifecycleScope)) {
                    viewModel.playbackControl()
                }
            }
        }
    }

    private fun handleBackButton() {
        binding.backButton.apply {
            setOnClickListener {
                Log.d("AddPlaylistFragment", "${findNavController().graph}")
                findNavController().popBackStack()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.playlists.observe(this) {
            Log.d("PlayerFragment", "${it.size}")
            playlistAdapter.submitList(it)
        }

        viewModel.playerState.observe(viewLifecycleOwner){
            Log.d("PlayerFragment","player state = ${it.name}")
            viewModel.render()
        }

        viewModel.screenState.observe(this) {
            it.render(binding)
        }
    }

    private fun parseIntent(adapterTrack: AdapterTrack): PlayerTrack {
        return adapterTrack.mapTrackToTrackForPlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    override fun onStop() {
        super.onStop()
        Log.d("PlayerFragment","onStop")
        viewModel.onStop()
    }

    override fun onResume() {
        super.onResume()
        Log.d("PlayerFragment","onResume")
        viewModel.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
                viewModel.onDestroy()
    }

    private fun initRecycler(){
//        playlistAdapter.onClickListener = {}
        playlistAdapter.showImage = {view, playlistImage ->
            val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
            val file = File(filePath, playlistImage)
            view.setImageURI(file.toUri())
        }
        binding.bottomSheetContainer.rvPlaylists .apply {
            adapter = playlistAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initBottomSheet(){
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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
                binding.overlay.alpha = slideOffset
            }
        })
    }
}