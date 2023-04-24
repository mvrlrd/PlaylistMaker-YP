package ru.mvrlrd.playlistmaker.ui.search


import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.*
import ru.mvrlrd.playlistmaker.Creator
import ru.mvrlrd.playlistmaker.PlayerActivity
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.ui.recycler.TrackAdapter
import ru.mvrlrd.playlistmaker.domain.Track
import ru.mvrlrd.playlistmaker.domain.TracksInteractor
import java.util.*
import kotlin.collections.ArrayList


class SearchActivity : ComponentActivity(), OnSharedPreferenceChangeListener {

    private lateinit var trackAdapter : TrackAdapter
    private lateinit var searchEditText: EditText
    private lateinit var clearIcon: ImageButton
    private lateinit var placeHolderMessage: TextView
    private lateinit var placeHolderImage: ImageView
    private lateinit var placeHolder : LinearLayout
    private lateinit var recyclerView:RecyclerView
    private lateinit var refreshButton: Button
    private lateinit var clearHistoryButton: Button
    private lateinit var youSearchedTitle: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private var isClickAllowed = true
    private lateinit var handler : Handler
    private var query = ""
    private lateinit var historySharedPreferences: SharedPreferences
    private val searchRunnable = Runnable {

//        search()
    }
    private lateinit var viewModel : SearchViewModel
    private val tracksInteractor = Creator.provideTracksInteractor(this)

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
            if (clearHistoryButton.visibility == View.VISIBLE) {
                val historyList = readTracksFromSearchedHistory()
                trackAdapter.setTracks(historyList)
            }
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
        if (query.isNotEmpty()){
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        youSearchedTitle = findViewById(R.id.youSearchedTitle)
        historySharedPreferences = getSharedPreferences(HISTORY_PREFERENCES, MODE_PRIVATE).apply {
            registerOnSharedPreferenceChangeListener(this@SearchActivity as OnSharedPreferenceChangeListener)
        }
        clearHistoryButton = findViewById<Button?>(R.id.clearHistoryButton).apply {
            setOnClickListener{
                clearHistory()
            }
        }
        trackAdapter = TrackAdapter{
            if (trackOnClickDebounce()) {
                navigateTo(PlayerActivity::class.java, it)
                hideTrackList()
                addToHistory(it)
            }
        }
        viewModel.tracksLiveData.observe(this){
            trackAdapter.setTracks(it as ArrayList<Track>)
        }

        handler = Handler(Looper.getMainLooper())

        placeHolderMessage = findViewById(R.id.placeholderMessage)
        placeHolderImage = findViewById(R.id.placeholderImage)
        placeHolder = findViewById(R.id.placeHolder)
        toolbar = findViewById<Toolbar>(R.id.searchToolbar).apply {
            setNavigationOnClickListener { onBackPressed() }
        }

        progressBar = findViewById(R.id.progressBar)

        searchEditText = findViewById<EditText?>(R.id.searchEditText)
            .apply {
                restoreTextFromBundle(textField = this, savedInstanceState = savedInstanceState)
            }.apply {
                setOnEditorActionListener { _, actionId, _ ->
                    onClickOnEnterOnVirtualKeyboard(actionId)
                }
            }.apply {
                doOnTextChanged { text, _, _, _ ->
                    query = text.toString()
                    if (query.isNotEmpty()){

                    }else{
                        progressBar.isVisible = false
                        if (searchEditText.hasFocus() && text?.isEmpty() == true) {
                            showHistory()
                        }
                    }
                    searchDebounce()
                    clearIcon.visibility = clearButtonVisibility(text)
                }
            }.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus && searchEditText.text.isEmpty()) showHistory()
                }
            }

        clearIcon = findViewById<ImageButton?>(R.id.clearTextButton).apply {
            setOnClickListener {
                searchEditText.text.clear()
                trackAdapter.setTracks(null)
                placeHolder.visibility = View.GONE
                searchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
                showHistory()
            }
        }
        refreshButton = findViewById<Button?>(R.id.refreshButton).apply {
            setOnClickListener {
//                search()
            }
        }
        recyclerView = findViewById<RecyclerView>(R.id.tracksRecyclerView).apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT, query)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        query = savedInstanceState.getString(INPUT_TEXT, "")
    }

    override fun onStop() {
        super.onStop()
        historySharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        showHistory()
    }

    private fun clearButtonVisibility(p0: CharSequence?) = if (p0.isNullOrEmpty()) View.GONE else View.VISIBLE

    private fun search() {
        hideTrackList()
        progressBar.isVisible = true
        tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer{
            override fun consume(foundTracks: List<Track>?, errorMessage: String?, code: Int) {
                handler.post {
                    progressBar.visibility = View.GONE
                    when (code) {
                        200 -> {
                            placeHolder.visibility = View.GONE
                            trackAdapter.setTracks(foundTracks as ArrayList<Track>)
                            if (foundTracks.isNullOrEmpty()) {
                                showMessage(getString(R.string.nothing_found), "")
                            }
                        }
                        else -> {
                            placeHolder.visibility = View.VISIBLE
                            showMessage(errorMessage!!, code.toString())
                        }
                    }
                }
            }
        })
    }
    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            val image = when (text) {
                getString(R.string.nothing_found) -> {
                    refreshButton.visibility = View.GONE
                    R.drawable.nothing_found
                }
                getString(R.string.error_connection) -> {
                    refreshButton.visibility = View.VISIBLE
                    R.drawable.connection_error
                }
                else -> {
                    R.drawable.connection_error
                }
            }
            placeHolderImage.setImageResource(image)
            placeHolderMessage.text = text
            placeHolder.visibility = View.VISIBLE
            trackAdapter.setTracks(null)
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeHolder.visibility = View.GONE
        }
    }

    private fun addToHistory(track: Track){
        val searchedTracks = readTracksFromSearchedHistory()
        if (searchedTracks.contains(track)){
            searchedTracks.remove(track)
        }
        searchedTracks.add(0,track)
        if (searchedTracks.size>10){
            searchedTracks.removeLast()
        }
        val json = Gson().toJson(searchedTracks)
        historySharedPreferences
            .edit()
            .putString(TRACK_LIST_KEY, json)
            .apply()
    }

    private fun clearHistory(){
        historySharedPreferences
            .edit()
            .clear()
            .apply()
        hideTrackList()
    }


    private fun hideTrackList(){
        trackAdapter.setTracks(null)
        clearHistoryButton.visibility = View.GONE
        youSearchedTitle.visibility = View.GONE
        placeHolder.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }

    private fun showHistory(){
        val historyList = readTracksFromSearchedHistory()
        placeHolder.visibility = View.GONE
        if (historyList.isNotEmpty()){
            clearHistoryButton.visibility = View.VISIBLE
            youSearchedTitle.visibility = View.VISIBLE
            trackAdapter.setTracks(historyList)
        }
    }

    private fun readTracksFromSearchedHistory(): ArrayList<Track>{
        val json = historySharedPreferences.getString(TRACK_LIST_KEY, null) ?: return arrayListOf()
        return Gson().fromJson(json, Array<Track>::class.java).toCollection(ArrayList())
    }

    private fun restoreTextFromBundle(textField: EditText, savedInstanceState: Bundle?){
        if (savedInstanceState != null) {
            query = savedInstanceState.getString(INPUT_TEXT)!!
            if (query.isNotEmpty()) {
                textField.setText(query)
//                search()
            }
        }
    }
    private fun onClickOnEnterOnVirtualKeyboard(actionId: Int): Boolean{
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (searchEditText.text.toString().isNotEmpty()) {
                hideTrackList()
                progressBar.isVisible = true
                viewModel.searchRequest(query)
//                search()
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
        private const val HISTORY_PREFERENCES = "history_preferences"
        private const val TRACK_LIST_KEY = "track_list_key"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}