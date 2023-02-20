package ru.mvrlrd.playlistmaker


import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import ru.mvrlrd.playlistmaker.model.Track
import ru.mvrlrd.playlistmaker.recycler.TrackAdapter
import ru.mvrlrd.playlistmaker.retrofit.ItunesApi
import ru.mvrlrd.playlistmaker.retrofit.TracksResponse
import java.util.*
import kotlin.collections.ArrayList


private const val INPUT_TEXT = "INPUT_TEXT"
private const val BASE_URL = "https://itunes.apple.com"
private const val HISTORY_PREFERENCES = "history_preferences"
private const val TRACK_LIST_KEY = "track_list_key"

class SearchActivity : AppCompatActivity(), TrackOnClickListener, OnSharedPreferenceChangeListener {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesApi::class.java)
    private val trackAdapter = TrackAdapter(this as TrackOnClickListener)
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

    private var query = ""
    private var lastQuery = ""
    private lateinit var historySharedPreferences: SharedPreferences

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
            if (clearHistoryButton.visibility == View.VISIBLE) {
                val historyList = readTracksFromSearchedHistory()
                trackAdapter.setTracks(historyList)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        youSearchedTitle = findViewById(R.id.youSearchedTitle)

        historySharedPreferences = getSharedPreferences(HISTORY_PREFERENCES, MODE_PRIVATE).apply {
            registerOnSharedPreferenceChangeListener(this@SearchActivity as OnSharedPreferenceChangeListener)
        }
        clearHistoryButton = findViewById<Button?>(R.id.clearHistoryButton).apply {
            setOnClickListener{
                historySharedPreferences
                    .edit()
                    .clear()
                    .apply()
                trackAdapter.setTracks(null)
                this.visibility = View.GONE
                youSearchedTitle.visibility = View.GONE
            }
        }
        placeHolderMessage = findViewById(R.id.placeholderMessage)
        placeHolderImage = findViewById(R.id.placeholderImage)
        placeHolder = findViewById(R.id.placeHolder)
        toolbar = findViewById<Toolbar>(R.id.searchToolbar).apply {
            setNavigationOnClickListener { onBackPressed() }
        }
        searchEditText = findViewById<EditText?>(R.id.searchEditText).apply {
            setOnEditorActionListener{
                    _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (searchEditText.text.toString().isNotEmpty()) {
                        lastQuery = searchEditText.text.toString()
                        search(lastQuery)
                        clearHistoryButton.visibility = View.GONE
                    }
                }
                false
            }
            if (savedInstanceState != null) {
                query = savedInstanceState.getString(INPUT_TEXT)!!
                if (query.isNotEmpty()) {
                    setText(query)
                    search(query)
                }
            }
        }
        clearIcon = findViewById<ImageButton?>(R.id.clearTextButton).apply {
            setOnClickListener {
                searchEditText.text.clear()
                trackAdapter.setTracks(null)
                placeHolder.visibility = View.GONE
                searchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)

                val historyList = readTracksFromSearchedHistory()
                if (historyList.isNotEmpty()){
                    clearHistoryButton.visibility = View.VISIBLE
                    youSearchedTitle.visibility = View.VISIBLE
                    trackAdapter.setTracks(historyList)
                }
            }
        }
        refreshButton = findViewById<Button?>(R.id.refreshButton).apply {
            setOnClickListener {
                searchEditText.setText(lastQuery)
                search(lastQuery)
            }
        }
        recyclerView = findViewById<RecyclerView>(R.id.tracksRecyclerView).apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
        searchEditText.doOnTextChanged { text, start, before, count ->
            query = text.toString()
            clearIcon.visibility = clearButtonVisibility(text)
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

    private fun clearButtonVisibility(p0: CharSequence?) = if (p0.isNullOrEmpty()) View.GONE else View.VISIBLE

    private fun search(query: String) {
        itunesService.search(query)
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>,
                                        response: Response<TracksResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.tracks?.isNotEmpty() == true) {
                                trackAdapter.setTracks(response.body()?.tracks!!)
                                placeHolder.visibility = View.GONE
                            } else {
                                showMessage(getString(R.string.nothing_found), "")
                            }
                        }
                        401 ->
                            showMessage(getString(R.string.authentication_troubles), response.code().toString())
                        else ->
                            showMessage(getString(R.string.error_connection), response.code().toString())
                    }
                }
                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showMessage(getString(R.string.error_connection), t.message.toString())
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

    override fun trackOnClick(track: Track) {
        writeTracksToSearchedHistory(track)


    }

    private fun writeTracksToSearchedHistory(track: Track){
        val searchedTracks = arrayListOf<Track>()
        searchedTracks.addAll(readTracksFromSearchedHistory())
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

    private fun readTracksFromSearchedHistory(): ArrayList<Track>{
        val json = historySharedPreferences.getString(TRACK_LIST_KEY, null) ?: return arrayListOf()
        return Gson().fromJson(json, Array<Track>::class.java).toCollection(ArrayList())
    }

}