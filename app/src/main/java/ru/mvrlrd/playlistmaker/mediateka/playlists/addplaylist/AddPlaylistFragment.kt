package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentAddPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPlaylistFragment : Fragment() {
    private var _binding: FragmentAddPlaylistBinding? =null
    private val binding get() = _binding?: throw RuntimeException("FragmentAddPlaylistBinding == null")
     private val viewModel: AddPlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.createPlaylistButton.setOnClickListener {
            Log.e("AddPlaylistFragment", "createPlaylistButton pressed")
        }
        initNameEditText()
        initDescriptionEditText()
        clearButtonHandle()
        return binding.root
    }

    private fun initNameEditText() {
        binding.nameEtContainer.nameEt
            .apply {
//                setOnEditorActionListener { _, actionId, _ ->
//                    onClickOnEnterOnVirtualKeyboard(actionId)
//                }
                doOnTextChanged { text, _, _, _ ->
                    if (this.hasFocus() && text.toString().isEmpty()) {

                    }
                    if (!text.isNullOrBlank()){
                      enableCreateButton()
                    }else{
                        disableCreateButton()
                    }
//                    viewModel.searchDebounce(binding.searchEditText.text.toString())
                    binding.nameEtContainer.clearTextButton.visibility = clearButtonVisibility(text.toString())
                }
            }
    }

    private fun disableCreateButton(){
        binding.createPlaylistButton.backgroundTintList = ColorStateList.valueOf(
            binding.createPlaylistButton.resources.getColor(
                R.color.bledniyFont,
                binding.createPlaylistButton.context.theme
            )
        )
        binding.createPlaylistButton.alpha = 0.5f
        binding.createPlaylistButton.isEnabled = false
    }
    private fun enableCreateButton(){
        binding.createPlaylistButton.backgroundTintList = ColorStateList.valueOf(
            binding.createPlaylistButton.resources.getColor(
                R.color.blue,
                binding.createPlaylistButton.context.theme
            )
        )
        binding.createPlaylistButton.alpha = 1f
        binding.createPlaylistButton.isEnabled = true
    }
    private fun clearButtonHandle(){
        binding.nameEtContainer.clearTextButton.apply {
            setOnClickListener {
                binding.nameEtContainer.nameEt.text.clear()
                binding.nameEtContainer.nameEt.onEditorAction(EditorInfo.IME_ACTION_DONE)
            }
        }
    }
    private fun initDescriptionEditText() {
        binding.descriptionEtContainer.nameEt
            .apply {
//                setOnEditorActionListener { _, actionId, _ ->
//                    onClickOnEnterOnVirtualKeyboard(actionId)
//                }
                doOnTextChanged { text, _, _, _ ->
                    if (this.hasFocus() && text.toString().isEmpty()) {

                    }
//                    viewModel.searchDebounce(binding.searchEditText.text.toString())
                    binding.descriptionEtContainer.clearTextButton.visibility = clearButtonVisibility(text.toString())
                }
            }
    }
    private fun clearButtonVisibility(p0: CharSequence?) =
        if (p0.isNullOrEmpty()) View.GONE else View.VISIBLE

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