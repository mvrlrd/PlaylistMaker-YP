package ru.mvrlrd.playlistmaker.mediateka.favorites


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentFavoritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.favorites.FavoriteAdapter
import ru.mvrlrd.playlistmaker.search.ui.SearchFragmentDirections
import ru.mvrlrd.playlistmaker.search.ui.SearchScreenState
import ru.mvrlrd.playlistmaker.search.ui.TrackAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? =null
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
        return binding.root
    }

    private fun initRecycler() {
        trackAdapter.apply {
            onClickListener = { track ->
                Unit
//                if (clickDebounce()) {
//                    viewModel.addToHistory(track)
//                    findNavController().navigate(
//                        SearchFragmentDirections.actionSearchFragmentToPlayerActivity(
//                            track
//                        )
//                    )
//                }
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.tracks.observe(this) { trackList ->
            trackAdapter.submitList(trackList)
        }

//        binding.placeHolder.placeholderMessage.text = this.resources.getText(R.string.mediateka_is_empty)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}