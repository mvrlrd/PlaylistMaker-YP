package ru.mvrlrd.playlistmaker.search.ui

import android.view.View
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentSearchBinding
import ru.mvrlrd.playlistmaker.search.domain.Track

sealed class SearchScreenState(val tracks: List<Track>? = null, val message: String? = null) {
    class Loading : SearchScreenState() {
        override fun render(binding: FragmentSearchBinding) {
            binding.progressBar.visibility = View.VISIBLE
            binding.errorPlaceholder.visibility= View.GONE
            binding.clearHistoryButton.visibility = View.GONE
            binding.youSearchedTitle.visibility = View.GONE
            binding.refreshButton.visibility = View.GONE
        }
    }

    class NothingFound : SearchScreenState() {
        override fun render(binding: FragmentSearchBinding) {
            binding.progressBar.visibility = View.GONE
            binding.errorPlaceholder.visibility = View.VISIBLE
            binding.clearHistoryButton.visibility = View.GONE
            binding.youSearchedTitle.visibility = View.GONE
            binding.refreshButton.visibility = View.GONE
            binding.infoPlaceholder.placeholderImage.setImageResource(R.drawable.nothing_found)
            binding.infoPlaceholder.placeholderMessage.text =
                binding.infoPlaceholder.placeholderMessage.resources.getText(R.string.nothing_found)
        }
    }

    class Error(val code: String, private val errorMessage: String?) : SearchScreenState(message = errorMessage) {
        override fun render(binding: FragmentSearchBinding) {
            binding.progressBar.visibility = View.GONE
            binding.errorPlaceholder.visibility = View.VISIBLE
            binding.clearHistoryButton.visibility = View.GONE
            binding.youSearchedTitle.visibility = View.GONE
            binding.refreshButton.visibility = View.VISIBLE
            binding.infoPlaceholder.placeholderImage.setImageResource(R.drawable.connection_error)
            binding.infoPlaceholder.placeholderMessage.text = errorMessage
        }
    }

    class Success(tracks: List<Track>?) : SearchScreenState(tracks = tracks) {
        override fun render(binding: FragmentSearchBinding) {
            binding.progressBar.visibility = View.GONE
            binding.errorPlaceholder.visibility = View.GONE
            binding.clearHistoryButton.visibility = View.GONE
            binding.youSearchedTitle.visibility = View.GONE
            binding.refreshButton.visibility = View.GONE
        }
    }

    class ShowHistory(tracks: List<Track>?) : SearchScreenState(tracks = tracks) {
        override fun render(binding: FragmentSearchBinding) {
            if (tracks.isNullOrEmpty()) {
                binding.clearHistoryButton.visibility = View.GONE
                binding.youSearchedTitle.visibility = View.GONE
            } else {
                binding.clearHistoryButton.visibility = View.VISIBLE
                binding.youSearchedTitle.visibility = View.VISIBLE
            }
            binding.progressBar.visibility = View.GONE
            binding.errorPlaceholder.visibility = View.GONE
            binding.refreshButton.visibility = View.GONE
        }
    }

    abstract fun render(binding: FragmentSearchBinding)
}

