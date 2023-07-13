package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist

import android.content.res.ColorStateList
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.doOnTextChanged
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentAddPlaylistBinding


sealed class AddPlaylistScreenState {

    object ClearNamFieldText : AddPlaylistScreenState() {
        override fun render(binding: FragmentAddPlaylistBinding) {
            binding.nameEtContainer.nameEt.text.clear()
            binding.nameEtContainer.nameEt.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }
    }

    object ClearDescriptionFieldText : AddPlaylistScreenState() {
        override fun render(binding: FragmentAddPlaylistBinding) {
            binding.descriptionEtContainer.nameEt.text.clear()
            binding.descriptionEtContainer.nameEt.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }
    }

    object InitEditTextFields : AddPlaylistScreenState() {
        override fun render(binding: FragmentAddPlaylistBinding) {
            initDescriptionEditText(
                binding.descriptionEtContainer.nameEt,
                binding.descriptionEtContainer.clearTextButton
            )
            initNameEditText(
                binding.nameEtContainer.nameEt,
                binding.nameEtContainer.clearTextButton,
                binding.createPlaylistButton
            )
        }
        private fun initDescriptionEditText(editText: EditText, clearTextButton: ImageButton) {
            editText.apply {
//                setOnEditorActionListener { _, actionId, _ ->
//                    onClickOnEnterOnVirtualKeyboard(actionId)
//                }
                    doOnTextChanged { text, _, _, _ ->
                        if (this.hasFocus() && text.toString().isEmpty()) {

                        }
//                    viewModel.searchDebounce(binding.searchEditText.text.toString())
                        clearTextButton.visibility = clearButtonVisibility(text.toString())
                    }
                }
        }

        private fun initNameEditText(
            editText: EditText, clearTextButton: ImageButton, createButton: Button
        ) {
            editText.apply {
//                setOnEditorActionListener { _, actionId, _ ->
//                    onClickOnEnterOnVirtualKeyboard(actionId)
//                }
                    doOnTextChanged { text, _, _, _ ->
                        if (this.hasFocus() && text.toString().isEmpty()) {

                        }
                        if (!text.isNullOrBlank()) {
                            enableCreateButton(createButton)
                        } else {
                            disableCreateButton(createButton)
                        }
//                    viewModel.searchDebounce(binding.searchEditText.text.toString())
                        clearTextButton.visibility = clearButtonVisibility(text.toString())
                    }
                }
        }

        private fun disableCreateButton(button: Button) {
            button.backgroundTintList = ColorStateList.valueOf(
                button.resources.getColor(
                    R.color.bledniyFont, button.context.theme
                )
            )
            button.alpha = 0.5f
            button.isEnabled = false
        }

        private fun enableCreateButton(button: Button) {
            button.backgroundTintList = ColorStateList.valueOf(
                button.resources.getColor(
                    R.color.blue, button.context.theme
                )
            )
            button.alpha = 1f
            button.isEnabled = true
        }


    }


    fun clearButtonVisibility(p0: CharSequence?) =
        if (p0.isNullOrEmpty()) View.GONE else View.VISIBLE

    abstract fun render(binding: FragmentAddPlaylistBinding)
}