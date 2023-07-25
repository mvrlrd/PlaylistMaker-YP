package ru.mvrlrd.playlistmaker.mediateka.playlists

import android.content.Context
import android.content.res.ColorStateList
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentAddPlaylistBinding
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import java.io.File
import java.io.FileOutputStream

 abstract class PlaylistEditingBaseFragment : Fragment() {
    private var _binding: FragmentAddPlaylistBinding? = null
     val binding
        get() = _binding ?: throw RuntimeException("PlaylistEditingBaseFragment == null")
    open val viewModel: HandlePlaylistBaseViewModel by viewModel()
     var _uri: Uri? = null
    lateinit var confirmDialog: MaterialAlertDialogBuilder
    lateinit var mediaPicker : ActivityResultLauncher<PickVisualMediaRequest>
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
            viewModel.changeSubmitButtonStatus(text.toString())
            if (!text.isNullOrEmpty()) {
                binding.playlistNameInputLayout.boxStrokeColor =
                    ResourcesCompat.getColor(this.resources, R.color.blue, requireActivity().theme)
                binding.playlistNameInputLayout.hintTextColor = ColorStateList.valueOf(
                    ResourcesCompat.getColor(
                        this.resources,
                        R.color.blue,
                        requireActivity().theme
                    )
                )
            }
        }
        binding.ietDesctiption.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.descriptionInputLayout.boxStrokeColor =
                    ResourcesCompat.getColor(this.resources, R.color.blue, requireActivity().theme)
                binding.descriptionInputLayout.hintTextColor = ColorStateList.valueOf(
                    ResourcesCompat.getColor(
                        this.resources,
                        R.color.blue,
                        requireActivity().theme
                    )
                )
            }
        }
        observeViewModel()
        initDialog()
        setOnClickListeners()
        registerImagePicker()

        binding.ivNewPlaylistImage.setOnClickListener {
            mediaPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        return binding.root
    }

    private fun initDialog() {
        confirmDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(resources.getText(R.string.quitting_question))
            setMessage(resources.getText(R.string.unsaved_data_caution))
            setNegativeButton(resources.getText(R.string.cancel)) { dialog, which ->
            }
            setPositiveButton(resources.getText(R.string.finish)) { dialog, which ->
                findNavController().navigateUp()
            }
        }
    }

    //TODO при перевороте экрана диалог не переживет этого, можно сделать fragment : DialogFragment


    private fun myHandleOnBackPressed() {
        if (viewModel.ifDataUnsaved(
                binding.ietPlaylistName.text.toString(),
                binding.ietDesctiption.text.toString(),
                _uri != null
            )
        ) {
            confirmDialog.show()
        } else {
            findNavController().popBackStack()
        }
    }



    private fun observeViewModel() {
        viewModel.screenState.observe(this) { screenState ->
            screenState.render(binding)
        }
    }

    private fun setOnClickListeners() {
        binding.btnBack.setOnClickListener {
            if (viewModel.ifDataUnsaved(
                    binding.ietPlaylistName.text.toString(),
                    binding.ietDesctiption.text.toString(),
                    _uri != null
                )
            ) {
                confirmDialog.show()
            } else {
                findNavController().popBackStack()
            }
        }
        binding.btnCreatePlaylist.setOnClickListener {
            val playlist = createPlaylist()
            Log.e(TAG,"path = ${playlist.playlistImagePath}")
            if (_uri!= null){
                saveImageToPrivateStorage(_uri!!, playlist.playlistImagePath)
                Log.e(TAG,"image file saved to storage")
            }

            viewModel.handlePlaylist(playlist)
            val text = this.resources.getString(R.string.playlist_created, playlist.name)
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

     open fun createPlaylist(): PlaylistForAdapter{
         val name = binding.ietPlaylistName.text.toString()
         val description = binding.ietDesctiption.text.toString()
         return PlaylistForAdapter(
             playlistId = null,
             name = name,
             description = description,
             playlistImagePath = viewModel.getImagePath(_uri != null),
         )
     }

    private fun saveImageToPrivateStorage(uri: Uri, nameOfImage: String) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                resources.getString(R.string.my_album_name))
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, nameOfImage)
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


     private fun registerImagePicker() {
         mediaPicker =
             registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                 if (uri != null) {
                     binding.ivNewPlaylistImage.setImageURI(uri)
                     _uri = uri
                 } else {
                     Log.d(TAG, "No media selected")
                 }
             }
     }

    companion object{
        const val TAG = "tag"
        private const val IMAGE_QUALITY = 30
    }
}