package ru.mvrlrd.playlistmaker.ui.search


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mvrlrd.playlistmaker.PlayerActivity
import ru.mvrlrd.playlistmaker.databinding.ActivitySearchBinding
import ru.mvrlrd.playlistmaker.ui.recycler.TrackAdapter
import ru.mvrlrd.playlistmaker.domain.Track


class SearchActivity : ComponentActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel : SearchViewModel
    private lateinit var trackAdapter : TrackAdapter
    private lateinit var handler : Handler
    private var isClickAllowed = true
    private val searchRunnable = Runnable {
        viewModel.searchRequest(binding.searchEditText.text.toString())
    }
    private fun trackOnClickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        if (binding.searchEditText.text.toString().isNotEmpty()){
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        viewModel.screenState.observe(this){screenState ->
            if ((screenState is SearchScreenState.Success
                && binding.searchEditText.text.isNotEmpty())
                ||(screenState !is SearchScreenState.Success)){
                trackAdapter.setTracks(screenState.tracks)
                screenState.render(binding)
            }
        }
        initRecycler()
        initEditText(savedInstanceState)
        initButtons()
        handler = Handler(Looper.getMainLooper())
        binding.searchToolbar.apply {
            setNavigationOnClickListener { onBackPressed() }
        }
    }

    private fun initButtons() {
        binding.clearTextButton.apply {
            setOnClickListener {
                //если прерываю поиск этой кнопкой то сначала покажетс истори а потом все равно результат поиска но поисковый запрос в строке булдет пустой
//                binding.progressBar.isVisible = false
//                handler.removeCallbacks(searchRunnable)

                binding.searchEditText.text.clear()
                binding.searchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
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
                    handler.removeCallbacks(searchRunnable)
                    viewModel.searchRequest(binding.searchEditText.text.toString())
                }
            }
        }
    }

    private fun initRecycler() {
        trackAdapter = TrackAdapter {
            if (trackOnClickDebounce()) {
                navigateTo(PlayerActivity::class.java, it)
                viewModel.addToHistory(it)
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
                    searchDebounce()
                    binding.clearTextButton.visibility = clearButtonVisibility(text.toString())
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT, binding.searchEditText.text.toString())
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.searchEditText.setText(savedInstanceState.getString(INPUT_TEXT, ""))
    }

    override fun onResume() {
        super.onResume()
        viewModel.showHistory()
    }
    private fun clearButtonVisibility(p0: CharSequence?) = if (p0.isNullOrEmpty()) View.GONE else View.VISIBLE



    private fun restoreTextFromBundle(textField: EditText, savedInstanceState: Bundle?){
        if (savedInstanceState != null) {
            if (savedInstanceState.getString(INPUT_TEXT)!!.isNotEmpty()) {
                textField.setText(savedInstanceState.getString(INPUT_TEXT)!!)
                viewModel.searchRequest(savedInstanceState.getString(INPUT_TEXT)!!)
            }
        }
    }
    private fun onClickOnEnterOnVirtualKeyboard(actionId: Int): Boolean{
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (binding.searchEditText.text.toString().isNotEmpty()) {
                handler.removeCallbacks(searchRunnable)
                viewModel.searchRequest(binding.searchEditText.text.toString())
            }
        }
        return false
    }

    private fun navigateTo(clazz: Class<out AppCompatActivity>, trackModel: Track) {
        val intent = Intent(this, clazz)
        intent.putExtra("my_track", trackModel)
        startActivity(intent)
    }

    companion object{
        private const val INPUT_TEXT = "INPUT_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}


