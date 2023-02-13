package ru.mvrlrd.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import ru.mvrlrd.playlistmaker.recycler.TrackAdapter
import ru.mvrlrd.playlistmaker.retrofit.ItunesApi
import ru.mvrlrd.playlistmaker.retrofit.TracksResponse


private const val INPUT_TEXT = "INPUT_TEXT"
private const val BASE_URL = "https://itunes.apple.com"

class SearchActivity : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesApi::class.java)
    private val trackAdapter = TrackAdapter()
    private lateinit var searchEditText: EditText
    private lateinit var clearIcon: ImageButton
    private lateinit var placeHolderMessage: TextView
    private lateinit var placeHolderImage: ImageView
    private lateinit var placeHolder : LinearLayout
    private lateinit var recyclerView:RecyclerView
    private lateinit var refreshButton: Button
    private var query = ""
    private var lastQuery = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        placeHolderMessage = findViewById(R.id.placeholderMessage)
        placeHolderImage = findViewById(R.id.placeholderImage)
        placeHolder = findViewById(R.id.placeHolder)
        findViewById<ImageView>(R.id.backButton).apply {
            setOnClickListener{
                onBackPressed()
            }
        }
        searchEditText = findViewById<EditText?>(R.id.searchEditText).apply {
            setOnEditorActionListener{
                    _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (searchEditText.text.toString().isNotEmpty()) {
                        lastQuery = searchEditText.text.toString()
                        search(lastQuery)
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
                trackAdapter.tracks = trackAdapter.tracks.apply { clear() }
                placeHolder.visibility = View.GONE
                searchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
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
                                trackAdapter.tracks = trackAdapter.tracks.apply {
                                    clear()
                                    addAll(response.body()?.tracks!!)
                                }
                                showMessage("", "")
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
            trackAdapter.tracks = trackAdapter.tracks.apply {
                clear()
            }
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeHolder.visibility = View.GONE
        }
    }
}