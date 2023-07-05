package ru.mvrlrd.playlistmaker.mediateka.favorites


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.databinding.FragmentFavoritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.favorites.FavoriteAdapter
import ru.mvrlrd.playlistmaker.mediateka.MediatekaFragmentDirections

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? =null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModel()
    private val trackAdapter: FavoriteAdapter by inject()
    private var isClickAllowed = true

    companion object {
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        initRecycler()
        return binding.root
    }

    private fun initRecycler() {
        trackAdapter.apply {
            onClickListener = { track ->
                if (clickDebounce()) {
                    findNavController().navigate(MediatekaFragmentDirections.actionMediatekaFragmentToPlayerActivity(track.apply { isFavorite = true }))
                }
            }
        }
        binding.favsRecyclerView.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onResume() {
        super.onResume()
        viewModel.loading()
        viewModel.updateFavorites()
//        binding.favsRecyclerView.itemAnimator = DefaultItemAnimator()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loading()
        viewModel.screenState.observe(viewLifecycleOwner){
            screenState ->
            screenState.render(binding)
        }
        viewModel.tracks.observe(this) { trackList ->
            trackAdapter.submitList(trackList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.onDestroy()
    }
}