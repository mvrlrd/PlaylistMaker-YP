package ru.mvrlrd.playlistmaker.ui.search

import android.view.View
import ru.mvrlrd.playlistmaker.databinding.ActivitySearchBinding
import ru.mvrlrd.playlistmaker.domain.Track

sealed class SearchScreenState(val message: String? = null, val tracks: List<Track>? = null)
    : Refresh
{
    class Loading : SearchScreenState(){
        override fun render(binding: ActivitySearchBinding) {
            binding.progressBar.visibility = View.VISIBLE
            binding.errorPlaceHolder.visibility = View.GONE
            binding.clearHistoryButton.visibility = View.GONE
            binding.youSearchedTitle.visibility = View.GONE
            binding.refreshButton.visibility = View.GONE
        }
    }
    class NothingFound : SearchScreenState(){
        override fun render(binding: ActivitySearchBinding) {
            binding.progressBar.visibility = View.GONE
            binding.errorPlaceHolder.visibility = View.VISIBLE
            binding.clearHistoryButton.visibility = View.GONE
            binding.youSearchedTitle.visibility = View.GONE
            binding.refreshButton.visibility = View.GONE
        }
    }
    class Error(message: String?) : SearchScreenState(message = message){
        override fun render(binding: ActivitySearchBinding) {
            binding.progressBar.visibility = View.GONE
            binding.errorPlaceHolder.visibility = View.VISIBLE
            binding.clearHistoryButton.visibility = View.GONE
            binding.youSearchedTitle.visibility = View.GONE
            binding.refreshButton.visibility = View.VISIBLE
        }
    }
    class Success(tracks: List<Track>?): SearchScreenState(tracks = tracks){
        override fun render(binding: ActivitySearchBinding) {
            binding.progressBar.visibility = View.GONE
            binding.errorPlaceHolder.visibility = View.GONE
            binding.clearHistoryButton.visibility = View.GONE
            binding.youSearchedTitle.visibility = View.GONE
            binding.refreshButton.visibility = View.GONE
        }
    }
    class ShowHistory(tracks: List<Track>?): SearchScreenState(tracks = tracks){
        override fun render(binding: ActivitySearchBinding) {
            binding.progressBar.visibility = View.GONE
            binding.errorPlaceHolder.visibility = View.GONE
            binding.clearHistoryButton.visibility = View.VISIBLE
            binding.youSearchedTitle.visibility = View.VISIBLE
            binding.refreshButton.visibility = View.GONE
        }
    }
}
interface Refresh{
    fun render(binding: ActivitySearchBinding)
}
