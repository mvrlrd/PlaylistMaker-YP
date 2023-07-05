package ru.mvrlrd.playlistmaker.mediateka.favorites

import android.view.View
import ru.mvrlrd.playlistmaker.databinding.FragmentFavoritesBinding

sealed class FavoriteScreenState {
    class Loading(): FavoriteScreenState(){
        override fun render(binding: FragmentFavoritesBinding) {
            binding.progressBar.visibility = View.VISIBLE
            binding.emptyFavoritesPlaceholder.visibility = View.GONE
        }
    }
    class Empty(): FavoriteScreenState(){
        override fun render(binding: FragmentFavoritesBinding) {
            binding.progressBar.visibility = View.GONE
            binding.emptyFavoritesPlaceholder.visibility = View.VISIBLE
        }
    }

    class Loaded(): FavoriteScreenState(){
        override fun render(binding: FragmentFavoritesBinding) {
            binding.progressBar.visibility = View.GONE
            binding.emptyFavoritesPlaceholder.visibility = View.GONE
        }
    }

    abstract fun render(binding: FragmentFavoritesBinding)
}