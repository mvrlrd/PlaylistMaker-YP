package ru.mvrlrd.playlistmaker.search.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mvrlrd.playlistmaker.databinding.FragmentSearchBinding


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
        binding.searchToolbar.apply {
            setNavigationOnClickListener { requireActivity().onBackPressed() }
        }
        viewModel.screenState.observe(requireActivity()) { screenState ->
            if (viewModel.isReadyToRender(screenState, binding.searchEditText.text.toString())) {
                trackAdapter.submitList(screenState.tracks)
                screenState.render(binding)
                if (screenState is SearchScreenState.Error) {
                    showToast(screenState.code)
                }
            }
        }
        initRecycler()
        initEditText(savedInstanceState)
        handleButtons()
    }

    override fun onResume() {
        super.onResume()
        if (binding.searchEditText.text.toString().isNotEmpty()) {
            viewModel.searchRightAway(binding.searchEditText.text.toString())
        } else {
            binding.tracksRecyclerView.itemAnimator = DefaultItemAnimator()
            viewModel.showHistory()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT, binding.searchEditText.text.toString())
    }


    private fun initRecycler() {
        trackAdapter.apply {
            onClickListener = { track ->
                if (viewModel.trackOnClickDebounce()) {
                    viewModel.addToHistory(track)
                    findNavController().navigate(
                        SearchFragmentDirections.actionSearchFragmentToPlayerActivity(
                            track
                        )
                    )
                }
            }
        }

        binding.tracksRecyclerView.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun initEditText(savedInstanceState: Bundle?) {
        binding.searchEditText
            .apply {
                restoreTextFromBundle(textField = this, savedInstanceState = savedInstanceState)
                setOnEditorActionListener { _, actionId, _ ->
                    onClickOnEnterOnVirtualKeyboard(actionId)
                }
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus && binding.searchEditText.text.isEmpty()) viewModel.showHistory()
                }
                doOnTextChanged { text, _, _, _ ->
                    if (binding.searchEditText.hasFocus() && text.toString().isEmpty()) {
                        viewModel.showHistory()
                    }
                    viewModel.searchDebounce(binding.searchEditText.text.toString())
                    binding.clearTextButton.visibility = clearButtonVisibility(text.toString())
                }
            }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun clearButtonVisibility(p0: CharSequence?) =
        if (p0.isNullOrEmpty()) View.GONE else View.VISIBLE

    private fun restoreTextFromBundle(textField: EditText, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getString(INPUT_TEXT)!!.isNotEmpty()) {
                textField.setText(savedInstanceState.getString(INPUT_TEXT)!!)
                viewModel.searchRightAway(savedInstanceState.getString(INPUT_TEXT)!!)
            }
        }
    }

    private fun onClickOnEnterOnVirtualKeyboard(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (binding.searchEditText.text.toString().isNotEmpty()) {
                viewModel.searchRightAway(binding.searchEditText.text.toString())
            }
        }
        return false
    }

    private fun handleButtons() {
        binding.clearTextButton.apply {
            setOnClickListener {
                binding.searchEditText.text.clear()
                binding.searchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
                binding.tracksRecyclerView.itemAnimator = null
                viewModel.showHistory()
            }
        }
        binding.clearHistoryButton.apply {
            setOnClickListener {
                viewModel.clearHistory()
            }
        }
        binding.refreshButton.apply {
            setOnClickListener {
                if (binding.searchEditText.text.toString().isNotEmpty()) {
                    viewModel.searchRightAway()
                }
            }
        }
    }

    companion object {
        private const val INPUT_TEXT = "INPUT_TEXT"
    }
}