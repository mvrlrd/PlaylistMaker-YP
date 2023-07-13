package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentAddPlaylistBinding
import ru.mvrlrd.playlistmaker.databinding.FragmentPlaylistsBinding


class AddPlaylistFragment : Fragment() {
    private var _binding: FragmentAddPlaylistBinding? =null
    private val binding get() = _binding?: throw RuntimeException("FragmentAddPlaylistBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

//    private fun initEditText() {
//        binding.nameEt
//            .apply {
////                setOnEditorActionListener { _, actionId, _ ->
////                    onClickOnEnterOnVirtualKeyboard(actionId)
////                }
//                doOnTextChanged { text, _, _, _ ->
//                    if (this.hasFocus() && text.toString().isEmpty()) {
//
//                    }
//                    viewModel.searchDebounce(binding.searchEditText.text.toString())
//                    binding.clearTextButton.visibility = clearButtonVisibility(text.toString())
//                }
//            }
//    }
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