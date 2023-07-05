package ru.mvrlrd.playlistmaker.mediateka.favorites

import android.view.View
import androidx.core.content.res.ResourcesCompat
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentFavoritesBinding

sealed class FavoriteScreenState {
    class Loading(): FavoriteScreenState(){
        override fun render(binding: FragmentFavoritesBinding) {
            binding.progressBar.visibility = View.VISIBLE
            binding.emptyFavoritesPlaceholder.visibility = View.GONE
            binding.favsRecyclerView.visibility = View.GONE
        }
    }
    class Empty(): FavoriteScreenState(){
        override fun render(binding: FragmentFavoritesBinding) {
            binding.progressBar.visibility = View.GONE
            binding.favsRecyclerView.visibility = View.GONE
            binding.emptyPlaceholder.placeholderMessage.text = binding.emptyPlaceholder.placeholderMessage.resources.getString(R.string.mediateka_is_empty)
            binding.emptyFavoritesPlaceholder.visibility = View.VISIBLE
        }
    }

    class Loaded(): FavoriteScreenState(){
        override fun render(binding: FragmentFavoritesBinding) {
            binding.favsRecyclerView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            binding.emptyFavoritesPlaceholder.visibility = View.GONE
        }
    }

    abstract fun render(binding: FragmentFavoritesBinding)
}