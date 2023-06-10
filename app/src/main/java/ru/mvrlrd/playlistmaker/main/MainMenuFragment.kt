package ru.mvrlrd.playlistmaker.main

import android.content.Intent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentMainMenuBinding
import ru.mvrlrd.playlistmaker.mediateka.MediatekaActivity
import ru.mvrlrd.playlistmaker.mediateka.MediatekaFragment
import ru.mvrlrd.playlistmaker.search.ui.SearchActivity
import ru.mvrlrd.playlistmaker.settings.ui.SettingsActivity

class MainMenuFragment : Fragment() {
    private var _binding: FragmentMainMenuBinding? = null
    private val binding: FragmentMainMenuBinding
        get() = _binding ?: throw RuntimeException("FragmentMainMenuBinding == null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButton.apply {
            setOnClickListener {
//                navigateTo(SearchActivity::class.java)
            }
        }
        binding.mediatekaButton.apply {
            setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.rootFragmentContainerView, MediatekaFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        }
        binding.settingsButton.apply {
            setOnClickListener {
//                navigateTo(SettingsActivity::class.java)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainMenuFragment()
    }
}
