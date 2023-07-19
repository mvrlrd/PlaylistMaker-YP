package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.ui

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.mvrlrd.playlistmaker.databinding.FragmentAddPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import java.io.File
import java.io.FileOutputStream

class AddPlaylistFragment : Fragment() {
    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentAddPlaylistBinding == null")
    private val viewModel: AddPlaylistViewModel by viewModel()
    private var _uri: Uri? = null
    lateinit var confirmDialog: MaterialAlertDialogBuilder
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                myHandleOnBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        binding.ietPlaylistName.doOnTextChanged { text, _, _, _ ->
            viewModel.handleCreateButtonVisibility(text.toString())
            if (!text.isNullOrEmpty()) {
                binding.playlistNameInputLayout.boxStrokeColor =
                    getColor(this.resources, R.color.blue, requireActivity().theme)
                binding.playlistNameInputLayout.hintTextColor = ColorStateList.valueOf(
                    getColor(
                        this.resources,
                        R.color.blue,
                        requireActivity().theme
                    )
                )
            }
        }
        binding.ietDesctiption.doOnTextChanged { text, _, _, _ ->
            viewModel.handleCreateButtonVisibility(text.toString())
            if (!text.isNullOrEmpty()) {
                binding.descriptionInputLayout.boxStrokeColor =
                    getColor(this.resources, R.color.blue, requireActivity().theme)
                binding.descriptionInputLayout.hintTextColor = ColorStateList.valueOf(
                    getColor(
                        this.resources,
                        R.color.blue,
                        requireActivity().theme
                    )
                )
            }
        }
        observeViewModel()
        confirmDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(this@AddPlaylistFragment.resources.getText(R.string.quitting_question))
            setMessage(this@AddPlaylistFragment.resources.getText(R.string.unsaved_data_caution))
            setNegativeButton(this@AddPlaylistFragment.resources.getText(R.string.cancel)) { dialog, which ->
            }
            setPositiveButton(this@AddPlaylistFragment.resources.getText(R.string.finish)) { dialog, which ->
                findNavController().navigateUp()
            }
        }
        setOnClickListeners()
        registerImagePicker()
        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("AddPlaylistFragment", "onDetach")
    }

    private fun myHandleOnBackPressed() {
        if (checkIfThereAreUnsavedData()) {
            confirmDialog.show()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun registerImagePicker() {
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Log.e("PhotoPicker", "bef")
                    binding.ivNewPlaylistImage.setImageURI(uri)
                    Log.e("PhotoPicker", "after")
                    _uri = uri
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        binding.ivNewPlaylistImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(this) { screenState ->
            screenState.render(binding)
        }
        viewModel.playlists.observe(this) {
            println("_____AddPlaylistFragment_____")
            it.forEach { println(it.playlistImagePath) }
            Log.e("AddPlaylistFragment", "${it.size}")
            println("_____AddPlaylistFragment_____")
        }
    }

    private fun setOnClickListeners() {

        binding.btnBack.setOnClickListener {
            if (checkIfThereAreUnsavedData()) {
                confirmDialog.show()
            } else {
                findNavController().popBackStack()
            }
        }
        binding.btnCreatePlaylist.setOnClickListener {
            val message = try {
                if (_uri == null) {
                    addPlaylist(false)
                } else {
                    saveImageToPrivateStorage(_uri!!, addPlaylist(true))
                }
                findNavController().popBackStack()
                this.resources.getString(R.string.playlist_created, binding.ietPlaylistName.text)
            } catch (e: Exception) {
                Log.e("AddPlaylistFragment","${e.message}")
                "error"
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }


    private fun checkIfThereAreUnsavedData(): Boolean {
        return (_uri != null
                || binding.ietPlaylistName.text.toString().isNotEmpty()
                || binding.ietDesctiption.text.toString().isNotEmpty())
    }

    private fun addPlaylist(isImageNotEmpty: Boolean): String {
        val name = binding.ietPlaylistName.text.toString()
        val description = binding.ietDesctiption.text.toString()
        val nameOfImage = if (isImageNotEmpty) {
            viewModel.generateImageNameForStorage()
        } else {
            ""
        }

        viewModel.addPlaylist(
            PlaylistForAdapter(
                name = name,
                description = description,
                playlistImagePath = nameOfImage
            )
        )
        return nameOfImage
    }

    private fun saveImageToPrivateStorage(uri: Uri, nameOfImage: String) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, nameOfImage)
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}