package ru.mvrlrd.playlistmaker.mediateka

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.mvrlrd.playlistmaker.mediateka.favorites.FavoritesFragment
import ru.mvrlrd.playlistmaker.mediateka.playlists.PlaylistsFragment

class MediatekaViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoritesFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }
}