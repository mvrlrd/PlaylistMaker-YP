package ru.mvrlrd.playlistmaker.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentMainMenuBinding

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
                findNavController().navigate(R.id.action_mainMenuFragment_to_searchFragment)
            }
        }
        binding.mediatekaButton.apply {
            setOnClickListener {
                findNavController().navigate(R.id.action_mainMenuFragment_to_mediatekaFragment)
            }
        }
        binding.settingsButton.apply {
            setOnClickListener {
                findNavController().navigate(R.id.action_mainMenuFragment_to_settingsFragment)
            }
        }
    }
}
