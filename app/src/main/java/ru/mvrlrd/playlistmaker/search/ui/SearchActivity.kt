package ru.mvrlrd.playlistmaker.search.ui


import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import ru.mvrlrd.playlistmaker.player.ui.PlayerActivity
import ru.mvrlrd.playlistmaker.databinding.ActivitySearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel : SearchViewModel by viewModel()
    private val trackAdapter : TrackAdapter by inject()
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchToolbar.apply {
            setNavigationOnClickListener { onBackPressed() }
        }
        viewModel.screenState.observe(this){screenState ->
            if (viewModel.isReadyToRender(screenState,binding.searchEditText.text.toString())){
                trackAdapter.submitList(screenState.tracks)
                screenState.render(binding)
                if (screenState is SearchScreenState.Error){
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
        if (binding.searchEditText.text.toString().isNotEmpty()){
            viewModel.searchRightAway(binding.searchEditText.text.toString())
        }else{
            binding.tracksRecyclerView.itemAnimator = DefaultItemAnimator()
            viewModel.showHistory()
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT, binding.searchEditText.text.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
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

    private fun initRecycler() {
        trackAdapter.apply {
            onClickListener = {track->
                if (viewModel.trackOnClickDebounce()) {
                    viewModel.addToHistory(track)
                    val intent = PlayerActivity.startPlayerActivity(this@SearchActivity, track)
                    startActivity(intent)
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

    private fun clearButtonVisibility(p0: CharSequence?) = if (p0.isNullOrEmpty()) View.GONE else View.VISIBLE

    private fun restoreTextFromBundle(textField: EditText, savedInstanceState: Bundle?){
        if (savedInstanceState != null) {
            if (savedInstanceState.getString(INPUT_TEXT)!!.isNotEmpty()) {
                textField.setText(savedInstanceState.getString(INPUT_TEXT)!!)
                viewModel.searchRightAway(savedInstanceState.getString(INPUT_TEXT)!!)
            }
        }
    }
    private fun onClickOnEnterOnVirtualKeyboard(actionId: Int): Boolean{
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (binding.searchEditText.text.toString().isNotEmpty()) {
                viewModel.searchRightAway(binding.searchEditText.text.toString())
            }
        }
        return false
    }
     private fun showToast(text: String){
        Toast.makeText(this, text,Toast.LENGTH_SHORT).show()
    }
    companion object{
        private const val INPUT_TEXT = "INPUT_TEXT"
    }
}


