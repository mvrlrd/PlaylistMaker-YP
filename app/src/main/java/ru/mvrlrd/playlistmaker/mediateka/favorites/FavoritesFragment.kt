package ru.mvrlrd.playlistmaker.mediateka.favorites


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.databinding.FragmentFavoritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.mediateka.MediatekaFragmentDirections
import ru.mvrlrd.playlistmaker.search.util.Debouncer

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModel()
    private val trackAdapter: FavoriteAdapter by inject()

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        initRecycler()
        observeScreenState()
        collectFavoriteTracks()
        return binding.root
    }

    private fun observeScreenState() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            screenState.render(binding)
        }
    }

    private fun collectFavoriteTracks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.trackList.collect() {
                if (it.isEmpty()) {
                    viewModel.emptyHistory()
                } else {
                    viewModel.loadHistory()
                }
                trackAdapter.submitList(it)
            }
        }
    }

    private fun initRecycler() {
        trackAdapter.apply {
            onClickListener = { track ->
                if (Debouncer().playClickDebounce(scope = lifecycleScope)) {
                    findNavController().navigate(
                        MediatekaFragmentDirections.actionMediatekaFragmentToPlayerFragment(track)
                    )
                }
            }
        }
        binding.favsRecyclerView.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}