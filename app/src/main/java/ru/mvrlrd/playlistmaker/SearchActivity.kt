package ru.mvrlrd.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import ru.mvrlrd.playlistmaker.model.Track
import ru.mvrlrd.playlistmaker.recycler.TrackAdapter
import ru.mvrlrd.playlistmaker.retrofit.ItunesApi
import ru.mvrlrd.playlistmaker.retrofit.TracksResponse

const val INPUT_TEXT = "INPUT_TEXT"
private const val BASE_URL = "https://itunes.apple.com"
class SearchActivity : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesApi::class.java)
    lateinit var searchEditText: EditText
    private val trackAdapter = TrackAdapter()

    var text = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }


        searchEditText = findViewById(R.id.searchEditText)
        val clearIcon = findViewById<ImageButton>(R.id.clearTextButton)

        searchEditText.setOnEditorActionListener {
                _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
               search()
            }
            false
        }

        if (savedInstanceState!=null){
            searchEditText.setText(savedInstanceState.getString(INPUT_TEXT))
        }

        clearIcon.setOnClickListener {
            searchEditText.text.clear()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.tracksRecyclerView).apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(this.context)
        }


        val simpleTextWatcher = object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearIcon.visibility = clearButtonVisibility(p0)
            }

            override fun afterTextChanged(p0: Editable?) {
                text = p0.toString()
                p0?.let{
                    if (it.isNotEmpty()) {
//                        trackAdapter.tracks = myApplication.trackDb.allTracks.filter { track ->
//                            track.artistName.startsWith(
//                                p0.toString(),
//                                ignoreCase = true
//                            )
//                        } as MutableList<Track>
                    } else {
                        trackAdapter.tracks.clear()
                    }
                    trackAdapter.notifyDataSetChanged()
                }
            }
        }

        searchEditText.addTextChangedListener(simpleTextWatcher)


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT, text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        text = savedInstanceState.getString(INPUT_TEXT, "")
    }

    private fun clearButtonVisibility(p0: CharSequence?) = if (p0.isNullOrEmpty()) View.GONE else View.VISIBLE

    private fun search() {
        itunesService.search(searchEditText.text.toString())
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>,
                                        response: Response<TracksResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.tracks?.isNotEmpty() == true) {
                                trackAdapter.tracks.clear()
                                trackAdapter.tracks.addAll(response.body()?.tracks!!)
                                trackAdapter.notifyDataSetChanged()
                                trackAdapter.tracks.forEach {
                                    println(it.trackName)
                                }
//                                showMessage("", "")
                            } else {
//                                showMessage(getString(R.string.nothing_found), "")
                            }
                        }
                        401 -> Unit
//                            showMessage(getString(R.string.authentication_troubles), response.code().toString())
                        else -> Unit
//                            showMessage(getString(R.string.something_went_wrong), response.code().toString())
                    }

                }
                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
//                    showMessage(getString(R.string.something_went_wrong), t.message.toString())
                }

            })
    }
}