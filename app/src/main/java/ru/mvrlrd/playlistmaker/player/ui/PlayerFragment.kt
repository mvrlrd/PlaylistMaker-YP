package ru.mvrlrd.playlistmaker.player.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentPlayerBinding
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.search.data.model.mapTrackToTrackForPlayer
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter
import ru.mvrlrd.playlistmaker.search.util.Debouncer
import ru.mvrlrd.playlistmaker.tools.loadPlaylist
import java.io.File


class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val playlistAdapter: PlaylistAdapterForPlayer by inject()
    private val args by navArgs<PlayerFragmentArgs>()
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(parseIntent(args.track))
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(layoutInflater, container, false)
        Log.d("PlayerFragment", "${args.track}")
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
        binding.fabOpenBottomSheet.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private fun handleAddToPlaylistButton1() {
        binding.bottomSheetContainer.btAddNewPlaylist.setOnClickListener {
            findNavController().navigate(
                PlayerFragmentDirections.actionPlayerFragmentToAddPlaylistFragment()
            )
        }
    }

    private fun handleLikeButton() {
        binding.fabAddToFavs.apply {
            setOnClickListener {
                if (Debouncer().playClickDebounce(this, lifecycleScope)) {
                    viewModel.handleLikeButton()
                }
            }
        }
    }

    private fun handlePlayButton() {
        binding.fabPlay.apply {
            setOnClickListener {
                if (Debouncer().playClickDebounce(this, lifecycleScope)) {
                    viewModel.playbackControl()
                }
            }
        }
    }

    private fun handleBackButton() {
        binding.btnBack.apply {
            setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun observeLikeChanging(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favoriteIds.collect() {
                viewModel.handleLike(it, args.track.trackId)
                Log.e("PlayerFragment", "Like changed")
            }
        }
    }

    private fun observeIsTrackInPlaylist(){
        viewModel.isTrackInPlaylist.observe(this) { pair ->
            val isTrackInPlaylist = pair.second
            val playlistName = pair.first
            val message = if (isTrackInPlaylist) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                this.resources.getString(R.string.track_added_to_playlist, playlistName)
            } else {
                this.resources.getString(R.string.track_already_added, playlistName)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun observePlaylists(){
        viewModel.playlists.observe(this) {
            playlistAdapter.submitList(it)
        }
    }

    private fun observeScreenState(){
        viewModel.screenState.observe(this) {
            if (it is PlayerScreenState.PlayerError) {
                it.render(binding)
                Toast.makeText(
                    requireContext(),
                    this.resources.getText(R.string.impossible_to_play),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                it.render(binding)
            }
        }
    }

    private fun observeViewModel() {
        observePlayerState()
        observePlaylists()
        observeLikeChanging()
        observeScreenState()
        observeIsTrackInPlaylist()
    }

    private fun observePlayerState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playerState.collect(){
                Log.e("PlayerFragment", it.name)
                viewModel.render(it)
            }
        }
//        viewModel.playerState.observe(viewLifecycleOwner) {
//            Log.d("PlayerFragment", "player state = ${it.name}")
//            viewModel.render()
//        }
    }

    private fun parseIntent(trackForAdapter: TrackForAdapter): PlayerTrack {
        return trackForAdapter.mapTrackToTrackForPlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        Log.d("PlayerFragment", "onStop")
        viewModel.onStop()
    }

    override fun onResume() {
        super.onResume()
        Log.d("PlayerFragment", "onResume")
        viewModel.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun initRecycler() {
        playlistAdapter.onClickListener = {
            lifecycleScope.launch {
                viewModel.addTrackToPlaylist(
                    trackId = args.track,
                    playlistId = it.playlistId!!
                )
            }
        }
        playlistAdapter.showImage = { view, playlistImagePath ->
            try {
                val filePath = File(
                    requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "myalbum"
                )
                val file = File(filePath, playlistImagePath)
                view.loadPlaylist(anySource = file, size = PLAYLIST_IMAGE_SIZE)
            } catch (e: Exception) {
                Log.e("PlayerFragment", e.message.toString())
            }
        }
        binding.bottomSheetContainer.rvPlaylists.apply {
            adapter = playlistAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initBottomSheet() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.bottomSheetContainer.bottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
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
    companion object{
        const val PLAYLIST_IMAGE_SIZE = 450
    }
}