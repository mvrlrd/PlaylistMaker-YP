package ru.mvrlrd.playlistmaker.mediateka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentMediatekaBinding

class MediatekaFragment : Fragment() {
    private var _binding: FragmentMediatekaBinding? = null
    private val binding: FragmentMediatekaBinding
        get() = _binding ?: throw RuntimeException("FragmentMediatekaBinding == null")

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediatekaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = MediatekaViewPagerAdapter(
            parentFragmentManager, lifecycle
        )

        tabMediator = TabLayoutMediator(binding.mediatekaTab, binding.viewPager){
                tab, position ->
            when(position){
                0 -> tab.text = this.resources.getText(R.string.favorites)
                1 -> tab.text = this.resources.getText(R.string.playlists)
            }
        }.apply {
            attach()
        }


        binding.mediatekaToolbar.apply {
            setNavigationOnClickListener { requireActivity().onBackPressed() }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MediatekaFragment()
    }
}