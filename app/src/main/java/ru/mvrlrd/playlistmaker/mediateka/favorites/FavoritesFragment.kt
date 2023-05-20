package ru.mvrlrd.playlistmaker.mediateka.favorites


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.ActivityMediatekaBinding
import ru.mvrlrd.playlistmaker.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? =null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private lateinit var viewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.placeHolder.placeholderMessage.text = this.resources.getText(R.string.mediateka_is_empty)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}