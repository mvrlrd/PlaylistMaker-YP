package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.mvrlrd.playlistmaker.databinding.FragmentAddPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPlaylistFragment : Fragment() {
    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentAddPlaylistBinding == null")
    private val viewModel: AddPlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        viewModel.initEditTextFields()
        observeViewModel()
        setOnClickListeners()
        return binding.root
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(this) { screenState ->
            screenState.render(binding)
        }
    }

    private fun setOnClickListeners() {
        binding.nameEtContainer.clearTextButton.apply {
            setOnClickListener {
                viewModel.clearNameFieldText()
            }
        }
        binding.descriptionEtContainer.clearTextButton.apply {
            setOnClickListener {
                viewModel.clearDescriptionFieldText()
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.createPlaylistButton.setOnClickListener {
            Log.e("AddPlaylistFragment", "createPlaylistButton pressed")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddPlaylistFragment()
    }
}