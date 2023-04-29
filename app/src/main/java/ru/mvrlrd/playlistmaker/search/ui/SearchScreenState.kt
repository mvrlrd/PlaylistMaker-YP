package ru.mvrlrd.playlistmaker.search.ui

import android.view.View
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.ActivitySearchBinding
import ru.mvrlrd.playlistmaker.search.domain.Track

sealed class SearchScreenState(val message: String? = null, val tracks: List<Track>? = null) {
    class Loading : SearchScreenState() {
        override fun render(binding: ActivitySearchBinding) {
            binding.progressBar.visibility = View.VISIBLE
            binding.errorPlaceHolder.visibility = View.GONE
            binding.clearHistoryButton.visibility = View.GONE
            binding.youSearchedTitle.visibility = View.GONE
            binding.refreshButton.visibility = View.GONE
        }
    }

    class NothingFound : SearchScreenState() {
        override fun render(binding: ActivitySearchBinding) {
            binding.progressBar.visibility = View.GONE
            binding.errorPlaceHolder.visibility = View.VISIBLE
            binding.clearHistoryButton.visibility = View.GONE
            binding.youSearchedTitle.visibility = View.GONE
            binding.refreshButton.visibility = View.GONE
            binding.placeholderImage.setImageResource(R.drawable.nothing_found)
            binding.placeholderMessage.text =
                binding.placeholderMessage.resources.getText(R.string.nothing_found)
        }
    }

    class Error(message: String?) : SearchScreenState(message = message) {
        override fun render(binding: ActivitySearchBinding) {
            binding.progressBar.visibility = View.GONE
            binding.errorPlaceHolder.visibility = View.VISIBLE
            binding.clearHistoryButton.visibility = View.GONE
            binding.youSearchedTitle.visibility = View.GONE
            binding.refreshButton.visibility = View.VISIBLE
            binding.placeholderImage.setImageResource(R.drawable.connection_error)
            binding.placeholderMessage.text = message
        }
    }

    class Success(tracks: List<Track>?) : SearchScreenState(tracks = tracks) {
        override fun render(binding: ActivitySearchBinding) {
            binding.progressBar.visibility = View.GONE
            binding.errorPlaceHolder.visibility = View.GONE
            binding.clearHistoryButton.visibility = View.GONE
            binding.youSearchedTitle.visibility = View.GONE
            binding.refreshButton.visibility = View.GONE
        }
    }

    class ShowHistory(tracks: List<Track>?) : SearchScreenState(tracks = tracks) {
        override fun render(binding: ActivitySearchBinding) {
            if (tracks.isNullOrEmpty()) {
                binding.clearHistoryButton.visibility = View.GONE
                binding.youSearchedTitle.visibility = View.GONE
            } else {
                binding.clearHistoryButton.visibility = View.VISIBLE
                binding.youSearchedTitle.visibility = View.VISIBLE
            }
            binding.progressBar.visibility = View.GONE
            binding.errorPlaceHolder.visibility = View.GONE
            binding.refreshButton.visibility = View.GONE
        }
    }

    abstract fun render(binding: ActivitySearchBinding)
}

