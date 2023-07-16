package ru.mvrlrd.playlistmaker.mediateka.favorites


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.databinding.FragmentFavoritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.mediateka.MediatekaFragmentDirections
import ru.mvrlrd.playlistmaker.search.ui.SearchFragmentDirections
import ru.mvrlrd.playlistmaker.search.util.Debouncer

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? =null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModel()
    private val trackAdapter: FavoriteAdapter by inject()

    companion object {
        fun newInstance() = FavoritesFragment()
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        initRecycler()
        viewModel.tracks.observe(this) { trackList ->
            trackAdapter.submitList(trackList)
        }
        return binding.root
    }

    private fun initRecycler() {
        trackAdapter.apply {
            onClickListener = { track ->
                if (Debouncer().playClickDebounce(scope = lifecycleScope)) {
                    findNavController().navigate(
                    MediatekaFragmentDirections.actionMediatekaFragmentToPlayerFragment(track.apply {
                        isFavorite = true
                    })
                    )
                }
            }
        }
        binding.favsRecyclerView.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.updateFavorites()
        binding.favsRecyclerView.itemAnimator = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.screenState.observe(viewLifecycleOwner){
            screenState ->
            screenState.render(binding)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.onDestroy()
    }
}