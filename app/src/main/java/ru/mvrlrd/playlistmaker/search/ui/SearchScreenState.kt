package ru.mvrlrd.playlistmaker.search.ui

import android.view.View
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentSearchBinding
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

sealed class SearchScreenState(
    val trackForAdapters: List<TrackForAdapter>? = null,
) {
    class Loading : SearchScreenState() {
        override fun render(binding: FragmentSearchBinding) {
            binding.progressBar.visibility = View.VISIBLE
            binding.placeholderContainer.visibility = View.GONE
            binding.btnClearHistory.visibility = View.GONE
            binding.tvHistoryTitle.visibility = View.GONE
            binding.btnRefresh.visibility = View.GONE
        }
    }

    class NothingFound : SearchScreenState() {
        override fun render(binding: FragmentSearchBinding) {
            binding.progressBar.visibility = View.GONE
            binding.placeholderContainer.visibility = View.VISIBLE
            binding.btnClearHistory.visibility = View.GONE
            binding.tvHistoryTitle.visibility = View.GONE
            binding.btnRefresh.visibility = View.GONE
            binding.infoPlaceholder.placeholderImage.setImageResource(R.drawable.nothing_found)
            binding.infoPlaceholder.placeholderMessage.text =
                binding.infoPlaceholder.placeholderMessage.resources.getText(R.string.nothing_found)
        }
    }

    class Error(val code: String, private val errorMessage: String?) :
        SearchScreenState() {
        override fun render(binding: FragmentSearchBinding) {
            binding.progressBar.visibility = View.GONE
            binding.placeholderContainer.visibility = View.VISIBLE
            binding.btnClearHistory.visibility = View.GONE
            binding.tvHistoryTitle.visibility = View.GONE
            binding.btnRefresh.visibility = View.VISIBLE
            binding.infoPlaceholder.placeholderImage.setImageResource(R.drawable.connection_error)
            binding.infoPlaceholder.placeholderMessage.text = errorMessage
        }
    }

    class Success(trackForAdapters: List<TrackForAdapter>?) :
        SearchScreenState(trackForAdapters = trackForAdapters) {
        override fun render(binding: FragmentSearchBinding) {
            binding.progressBar.visibility = View.GONE
            binding.btnClearHistory.visibility = View.GONE
            binding.tvHistoryTitle.visibility = View.GONE
            binding.btnRefresh.visibility = View.GONE
            if (trackForAdapters.isNullOrEmpty()) {
                binding.placeholderContainer.visibility = View.VISIBLE
                binding.infoPlaceholder.placeholderImage.setImageResource(R.drawable.nothing_found)
                binding.infoPlaceholder.placeholderMessage.text = binding.infoPlaceholder.placeholderMessage.resources.getText(R.string.history_is_empty)
            } else {
                binding.placeholderContainer.visibility = View.GONE
            }
        }
    }

    class ShowHistory(trackForAdapters: List<TrackForAdapter>?) :
        SearchScreenState(trackForAdapters = trackForAdapters) {
        override fun render(binding: FragmentSearchBinding) {
            if (trackForAdapters.isNullOrEmpty()) {
                binding.placeholderContainer.visibility = View.VISIBLE
                binding.infoPlaceholder.placeholderMessage.text = binding.infoPlaceholder.placeholderMessage.resources.getText(R.string.history_is_empty)
                binding.btnClearHistory.visibility = View.GONE
                binding.tvHistoryTitle.visibility = View.GONE
            } else {
                binding.placeholderContainer.visibility = View.GONE
                binding.btnClearHistory.visibility = View.VISIBLE
                binding.tvHistoryTitle.visibility = View.VISIBLE
            }
            binding.progressBar.visibility = View.GONE

            binding.btnRefresh.visibility = View.GONE
        }
    }

    abstract fun render(binding: FragmentSearchBinding)
}

