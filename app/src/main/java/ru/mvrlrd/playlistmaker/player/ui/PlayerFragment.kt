package ru.mvrlrd.playlistmaker.player.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
import ru.mvrlrd.playlistmaker.tools.loadImage


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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomSheet()
        observeViewModel()
        handleBackButton()
        handlePlayButton()
        handleLikeButton()
        handleAddToPlaylistButton()
        setOnClickToNavigateAddingPlaylistFragment()
        initRecycler()
    }

    private fun handleAddToPlaylistButton() {
        binding.fabOpenBottomSheet.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private fun setOnClickToNavigateAddingPlaylistFragment() {
        binding.bottomSheetContainer.btAddNewPlaylist.setOnClickListener {
            viewModel.putItOnBackground()
            findNavController().navigate(
                PlayerFragmentDirections.actionPlayerFragmentToAddPlaylistFragment()
            )
        }
    }

    private fun handleLikeButton() {
        binding.fabAddToFavs.apply {
            setOnClickListener {
                if (Debouncer().playClickDebounce(this, lifecycleScope)) {
                    viewModel.addOrRemoveFromFavorites()
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

    private fun observeIsTrackInPlaylist(){
        viewModel.isTrackInPlaylist.observe(this) { result ->
            val isTrackInPlaylist = result.wasAdded
            val playlistName = result.playlistName
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playlists.collect(){
                playlistAdapter.submitList(it)
            }
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
        observeScreenState()
        observeIsTrackInPlaylist()
    }

    private fun observePlayerState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
            viewModel.playerState.collect(){
//                Log.d(TAG, "observePlayerState: observing  ${it.name}")
                    viewModel.render(it)
                }
            }
        }
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
        viewModel.onStop()
    }

    override fun onResume() {
        super.onResume()
//        viewModel.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun initRecycler() {
        playlistAdapter.apply {
            onClickListener = {
                    viewModel.addTrackToPlaylist(
                        track = args.track,
                        playlist = it
                    )
            }
            showImage = { view: ImageView, path: String ->
                val file = viewModel.getFile(path, resources.getString(R.string.my_album_name))
                view.loadImage(
                    file,
                    size = resources.getInteger(R.integer.picture_small_size),
                    radius = resources.getDimensionPixelSize(R.dimen.radius_small)
                )
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
        private const val TAG = "PlayerFragment"
    }
}