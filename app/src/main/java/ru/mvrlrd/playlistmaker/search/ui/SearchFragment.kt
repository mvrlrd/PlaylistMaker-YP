package ru.mvrlrd.playlistmaker.search.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.databinding.FragmentSearchBinding
import ru.mvrlrd.playlistmaker.search.util.Debouncer

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchBinding == null")
    private val viewModel: SearchViewModel by viewModel()
    private val trackAdapter: TrackAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.screenState.observe(this) { screenState ->
            if (viewModel.isReadyToRender(screenState, binding.etSearchField.text.toString())) {
                trackAdapter.submitList(screenState.trackForAdapters)
                screenState.render(binding)
                if (screenState is SearchScreenState.Error) {
                    showToast(screenState.code)
                }
            } else {
                screenState.render(binding)
            }
        }
        initRecycler()
        initEditText()
        handleButtons()
    }

    override fun onResume() {
        super.onResume()
        if (binding.etSearchField.text.toString().isEmpty()) {
            binding.rvTracks.itemAnimator = DefaultItemAnimator()
            viewModel.showHistory()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.onDestroy()
    }

    private fun initRecycler() {
        trackAdapter.apply {
            onClickListener = { track ->
                if (Debouncer().playClickDebounce(scope = lifecycleScope)) {
                    viewModel.addToHistory(track)
                    findNavController().navigate(
                        SearchFragmentDirections.actionSearchFragmentToPlayerFragment(track)
                    )
                }
            }
        }

        binding.rvTracks.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun initEditText() {
        binding.etSearchField
            .apply {
                setOnEditorActionListener { _, actionId, _ ->
                    onClickOnEnterOnVirtualKeyboard(actionId)
                }
                doOnTextChanged { text, _, _, _ ->
                    if (binding.etSearchField.hasFocus() && text.toString().isEmpty()) {
                        viewModel.showHistory()
                    }
                    viewModel.searchDebounce(binding.etSearchField.text.toString())
                    binding.btnClearText.visibility = clearButtonVisibility(text.toString())
                }
            }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun clearButtonVisibility(p0: CharSequence?) =
        if (p0.isNullOrEmpty()) View.GONE else View.VISIBLE

    private fun onClickOnEnterOnVirtualKeyboard(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (binding.etSearchField.text.toString().isNotEmpty()) {
                viewModel.searchRequest(binding.etSearchField.text.toString())
            }
        }
        return false
    }

    private fun handleButtons() {
        binding.btnClearText.apply {
            setOnClickListener {
                binding.etSearchField.text.clear()
                binding.etSearchField.onEditorAction(EditorInfo.IME_ACTION_DONE)
                binding.rvTracks.itemAnimator = null
                viewModel.showHistory()
            }
        }
        binding.btnClearHistory.apply {
            setOnClickListener {
                viewModel.clearHistory()
            }
        }
        binding.btnRefresh.apply {
            setOnClickListener {
                if (binding.etSearchField.text.toString().isNotEmpty()) {
                    viewModel.searchRequest(binding.etSearchField.text.toString())
                }
            }
        }
    }
}