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
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mvrlrd.playlistmaker.PlayerActivity
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.ActivitySearchBinding
import ru.mvrlrd.playlistmaker.ui.recycler.TrackAdapter
import ru.mvrlrd.playlistmaker.domain.Track
import kotlin.collections.ArrayList

class SearchActivity : ComponentActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel : SearchViewModel
    private lateinit var trackAdapter : TrackAdapter
    private lateinit var handler : Handler
    private var isClickAllowed = true
    private val searchRunnable = Runnable {
        hideTrackList()
        binding.progressBar.isVisible = true
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
        viewModel.toast.observe(this){
            when(it){
               is PlaceHolderState.NothingFound->{
                   binding.progressBar.visibility = View.GONE
                   binding.refreshButton.visibility = View.GONE
                  val image = R.drawable.nothing_found
                   binding.placeholderImage.setImageResource(image)
                   binding.placeholderMessage.text = "Not"
                   binding.placeHolder.visibility = View.VISIBLE
                   viewModel.clearTrackList()
               }
               is PlaceHolderState.Loading->{
                   viewModel.clearTrackList()
                   binding.progressBar.visibility = View.VISIBLE
               }
               is PlaceHolderState.Error->{
                   binding.progressBar.visibility = View.GONE
                   binding.placeHolder.visibility = View.VISIBLE
                   val image = R.drawable.connection_error
                   binding.placeholderImage.setImageResource(image)
                   binding.placeholderMessage.text = it.message
               }
                is PlaceHolderState.Empty->{
                    binding.progressBar.visibility = View.GONE
                    binding.placeHolder.visibility = View.GONE
                }
            }
//            showMessage(it, "fe")
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
                binding.searchEditText.text.clear()
                trackAdapter.setTracks(null)
                binding.placeHolder.visibility = View.GONE
                binding.searchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
                showHistory()
            }
        }
        binding.clearHistoryButton.apply {
            setOnClickListener {
                clearHistory()
            }
        }
        binding.refreshButton.apply {
            setOnClickListener {
                if (binding.searchEditText.text.toString().isNotEmpty()) {
                    viewModel.searchRequest(binding.searchEditText.text.toString())
                }
            }
        }
    }

    private fun initRecycler() {
        trackAdapter = TrackAdapter {
            if (trackOnClickDebounce()) {
                navigateTo(PlayerActivity::class.java, it)
                hideTrackList()
                viewModel.addToHistory(it)
            }
        }
        binding.tracksRecyclerView.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
        viewModel.tracksLiveData.observe(this) {
            binding.progressBar.isVisible = false
            if (it.isNullOrEmpty()) {
                trackAdapter.setTracks(null)
            } else {
                trackAdapter.setTracks(it as ArrayList<Track>)
            }
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
                    if (hasFocus && binding.searchEditText.text.isEmpty()) showHistory()
                }
                doOnTextChanged { text, _, _, _ ->
                    if (binding.searchEditText.text.toString().isEmpty()) {
                        binding.progressBar.isVisible = false
                        if (binding.searchEditText.hasFocus() && text?.isEmpty() == true) {
                            showHistory()
                        }
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
        showHistory()
    }
    private fun clearButtonVisibility(p0: CharSequence?) = if (p0.isNullOrEmpty()) View.GONE else View.VISIBLE

    private fun clearHistory(){
        viewModel.clearHistory()
        hideTrackList()
    }

    private fun hideTrackList(){
        viewModel.clearTrackList()
        binding.clearHistoryButton.visibility = View.GONE
        binding.youSearchedTitle.visibility = View.GONE
        binding.placeHolder.visibility = View.GONE
        binding.refreshButton.visibility = View.GONE
    }

    private fun showHistory(){
        val historyList = viewModel.getHistory()
        binding.placeHolder.visibility = View.GONE
        if (historyList.isNotEmpty()){
            binding.clearHistoryButton.visibility = View.VISIBLE
            binding.clearTextButton.visibility = View.VISIBLE
            binding.youSearchedTitle.visibility = View.VISIBLE
            trackAdapter.setTracks(historyList as ArrayList<Track>)
        }
    }

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
                hideTrackList()
                binding.progressBar.isVisible = true
                viewModel.searchRequest(binding.searchEditText.text.toString())
            }
        }
        return false
    }
    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            val image = when (text) {
                getString(R.string.nothing_found) -> {
                    binding.refreshButton.visibility = View.GONE
                    R.drawable.nothing_found
                }
                getString(R.string.error_connection) -> {
                    binding.refreshButton.visibility = View.VISIBLE
                    R.drawable.connection_error
                }
                else -> {
                    R.drawable.connection_error
                }
            }
            binding.placeholderImage.setImageResource(image)
            binding.placeholderMessage.text = text
            binding.placeHolder.visibility = View.VISIBLE
            trackAdapter.setTracks(null)
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            binding.placeHolder.visibility = View.GONE
        }
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


