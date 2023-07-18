package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.ui

import android.content.Context
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
import androidx.core.content.res.ResourcesCompat
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
        viewModel.initEditTextFields()
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

//        val dmyDawable = ResourcesCompat.getDrawable(
//            resources,
//            R.drawable.test____,
//            requireActivity().theme
//        )
//        binding.ivNewPlaylistImage.setImageDrawable(dmyDawable)


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
                    binding.ivNewPlaylistImage.setImageURI(uri)
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
            println("__________")
            it.forEach { println(it) }
            Log.d("AddPlaylistFragment", "${it.size}")
            println("__________")
        }
    }

    private fun setOnClickListeners() {
        binding.nameEtContainer.btnClearText.apply {
            setOnClickListener {
                viewModel.clearNameFieldText()
            }
        }
        binding.descriptionEtContainer.btnClearText.apply {
            setOnClickListener {
                viewModel.clearDescriptionFieldText()
            }
        }
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
                this.resources.getString(R.string.playlist_created, binding.nameEtContainer.nameEt.text)
            } catch (e: Exception) {
                "error"
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }


    private fun checkIfThereAreUnsavedData(): Boolean {
        return (_uri != null
                || binding.nameEtContainer.nameEt.text.toString().isNotEmpty()
                || binding.descriptionEtContainer.nameEt.text.toString().isNotEmpty())
    }

    private fun addPlaylist(isImageNotEmpty: Boolean): String {
        val name = binding.nameEtContainer.nameEt.text.toString()
        val description = binding.descriptionEtContainer.nameEt.text.toString()
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