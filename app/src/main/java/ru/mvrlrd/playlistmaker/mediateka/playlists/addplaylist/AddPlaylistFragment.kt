package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import ru.mvrlrd.playlistmaker.databinding.FragmentAddPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

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

        registerImagePicker()

        return binding.root
    }

    private fun registerImagePicker() {
        //регистрируем событие, которое вызывает photo picker
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.albumImageView.setImageURI(uri)
    //                    saveImageToPrivateStorage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        binding.albumImageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
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
            setPickedImage()
            Toast.makeText(requireContext(), "плейлист ${binding.nameEtContainer.nameEt.text} создан", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setPickedImage() {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        val file = File(filePath, viewModel.generateImageNameForStorage())
        binding.albumImageView.setImageURI(file.toUri())
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