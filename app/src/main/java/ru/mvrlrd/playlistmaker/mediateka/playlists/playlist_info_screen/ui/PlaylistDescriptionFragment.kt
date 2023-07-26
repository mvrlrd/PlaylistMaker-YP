package ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.ui


import android.content.Intent
import android.os.Bundle
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentPlaylistDescriptionBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfo
import ru.mvrlrd.playlistmaker.search.ui.TrackAdapter
import ru.mvrlrd.playlistmaker.search.util.Debouncer
import ru.mvrlrd.playlistmaker.tools.loadPlaylistImageNEW


class PlaylistDescriptionFragment : Fragment() {
    private var _binding: FragmentPlaylistDescriptionBinding? = null
    private val binding: FragmentPlaylistDescriptionBinding get() = _binding!!
    private val args by navArgs<PlaylistDescriptionFragmentArgs>()
    private lateinit var playlistInfo: PlaylistInfo

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var additionMenuBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val trackAdapter: TrackAdapter by inject()

    lateinit var confirmDialog: MaterialAlertDialogBuilder


    private val viewModel: PlaylistInfoViewModel by viewModel {
        parametersOf(args.playlist.playlistId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDescriptionBinding.inflate(layoutInflater, container, false)
        observeViewModel()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.ivSharePlaylist.setOnClickListener {
            sharePlaylist()
        }

        binding.ivPlaylistMenu.setOnClickListener {
            additionMenuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allSongsDebugging.collect(){
                println()
                it.forEach {
                    println(it)
                }
                println()
            }
        }
        initAdditionMenuBottomSheet()

        initRecycler()

        initBottomSheet()
        return binding.root
    }

    private fun sharePlaylist() {
        if (trackAdapter.isListEmpty()) {
            Toast.makeText(
                requireActivity(),
                resources.getText(R.string.there_is_nothing_to_share),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getTextForPlaylistSharing())
                type = INTENT_TYPE_FOR_SENDING
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun initRecycler() {
        binding.bottomSheetContainerForPlaylist.rvPlaylists.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
        trackAdapter.apply {
            onClickListener = { track ->
                if (Debouncer().playClickDebounce(scope = lifecycleScope)) {
                    findNavController().navigate(
                        PlaylistDescriptionFragmentDirections.actionPlaylistDescriptionFragmentToPlayerFragment(
                            track
                        )
                    )
                }
            }
            onLongClickListener = { track ->
                initDialog(track.trackId).show()
            }
        }
    }

    private fun initDialogDeletePlaylist(): MaterialAlertDialogBuilder {
        confirmDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(resources.getText(R.string.delete_playlist_dialog_title))
            setMessage(resources.getText(R.string.delete_playlist_dialog_message))
            setNegativeButton(resources.getText(R.string.cancel)) { dialog, which ->
            }
            setPositiveButton(resources.getText(R.string.delete_playlist_dialog_positive)) { dialog, which ->
                playlistInfo.playlist.playlistId?.let { viewModel.deletePlaylist(it) }
                Toast.makeText(requireContext(), "Плейлист ${playlistInfo.playlist.name} удален", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
        return confirmDialog
    }

    private fun initDialog(trackId: Long): MaterialAlertDialogBuilder {
        confirmDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(resources.getText(R.string.deleting_track_question))
            setNegativeButton(resources.getText(R.string.deleting_track_negative_answer)) { dialog, which ->
            }
            setPositiveButton(resources.getText(R.string.deleting_track_positive_answer)) { dialog, which ->
                viewModel.deleteTrackFromPlaylist(trackId, args.playlist.playlistId!!)
            }
        }
        return confirmDialog
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.playlistInfo.collect(){
                viewModel.changeState(it)
                trackAdapter.submitList(it.songs)
                playlistInfo = it
                initPlaylistInfo()
                binding.ivPlaylistImage.loadPlaylistImageNEW(it.playlist.playlistImagePath)
            }
        }
        viewModel.screenState.observe(this){
            it.render(binding)
        }
    }

    private fun initBottomSheet() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.bottomSheetContainerForPlaylist.bottomSheetForPlaylist).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
                peekHeight = (800).toInt()
            }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.alpha = 0.01f
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }
        })
    }

    private fun initAdditionMenuBottomSheet() {
        initAdditionBottomSheetButtons()
        additionMenuBottomSheetBehavior =
            BottomSheetBehavior.from(binding.bottomSheetAdditionMenuContainer.additionMenuBottomSheet)
                .apply {
                    state = BottomSheetBehavior.STATE_HIDDEN
                    addBottomSheetCallback(object :
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
                            binding.overlay.alpha = slideOffset + 1f
                        }
                    })
                }
    }

    private fun initAdditionBottomSheetButtons() {
        binding.bottomSheetAdditionMenuContainer.tvSharePlaylist.setOnClickListener {
            sharePlaylist()
        }
        binding.bottomSheetAdditionMenuContainer.tvDeletePlaylist.setOnClickListener {
            initDialogDeletePlaylist().show()
        }
        binding.bottomSheetAdditionMenuContainer.tvEditInfo.setOnClickListener {
            navigateToEditPlaylist()
        }
    }

    private fun navigateToEditPlaylist() {
        findNavController().navigate(
            PlaylistDescriptionFragmentDirections.actionPlaylistDescriptionFragmentToEditPlaylistFragment(
                playlistInfo.playlist
            )
        )
    }

    private fun initPlaylistInfo() {
        binding.bottomSheetAdditionMenuContainer.playlistItem.tvPlaylistName.text =
            playlistInfo.playlist.name
        binding.bottomSheetAdditionMenuContainer.playlistItem.tvQuantityOfTracks.text =
            resources.getQuantityString(
                R.plurals.plural_tracks,
                playlistInfo.songs.size,
                playlistInfo.songs.size
            )
        binding.bottomSheetAdditionMenuContainer.playlistItem.ivPlaylistImage.loadPlaylistImageNEW(playlistInfo.playlist.playlistImagePath)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getTextForPlaylistSharing():String {
        val str = mutableListOf<String>()
        str.add(playlistInfo.playlist.name)
        if (playlistInfo.playlist.description.isNotBlank()) {
            str.add(playlistInfo.playlist.description)
        }
        str.add(
            this.resources.getQuantityString(
                R.plurals.plural_tracks,
                playlistInfo.songs.size,
                playlistInfo.songs.size
            )
        )
        for (track in playlistInfo.songs) {
            str.add("${playlistInfo.songs.indexOf(track) + 1}. $track")
        }
        return str.joinToString("\n")
    }

    companion object{
        private const val TAG = "PlaylistDescriptionFragment"
        private const val INTENT_TYPE_FOR_SENDING = "text/plain"
    }

}