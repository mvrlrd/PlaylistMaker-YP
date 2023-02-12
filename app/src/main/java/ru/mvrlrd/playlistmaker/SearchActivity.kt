package ru.mvrlrd.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
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
    private val trackAdapter = TrackAdapter()
    private lateinit var searchEditText: EditText
    private lateinit var clearIcon: ImageButton
    private lateinit var placeHolderMessage: TextView
    private lateinit var placeHolderImage: ImageView
    private lateinit var placeHolder : LinearLayout
    private lateinit var recyclerView:RecyclerView
    private var text = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        placeHolderMessage = findViewById(R.id.placeholderMessage)
        placeHolderImage = findViewById(R.id.placeholderImage)
        placeHolder = findViewById(R.id.placeHolder)

        findViewById<ImageView>(R.id.backButton).also {
            it.setOnClickListener{
                onBackPressed()
            }
        }

        searchEditText = findViewById<EditText?>(R.id.searchEditText).also {
            it.setOnEditorActionListener{
                    _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (searchEditText.text.toString().isNotEmpty()) {
                        search()
                    }
                }
                false
            }
            if (savedInstanceState!=null){
                it.setText(savedInstanceState.getString(INPUT_TEXT))
            }
        }

        clearIcon = findViewById<ImageButton?>(R.id.clearTextButton).also {
            it.setOnClickListener {
                searchEditText.text.clear()
                println("OIIIIIIIIIIIIIIIIII")
                trackAdapter.tracks.clear()
                trackAdapter.notifyDataSetChanged()
                showMessage("","")
                searchEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
//
            }

        }

        recyclerView = findViewById<RecyclerView>(R.id.tracksRecyclerView).apply {
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
//                        trackAdapter.tracks.clear()
                    }
//                    trackAdapter.notifyDataSetChanged()
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
                    showMessage(getString(R.string.something_went_wrong), t.message.toString())
                }

            })
    }

    private fun showMessage(text: String, additionalMessage: String) {

        if (text.isNotEmpty()) {
            val image = when (text) {
                getString(R.string.nothing_found) -> {
                    R.drawable.nothing_found
                }
                getString(R.string.error_connection) -> {
                    R.drawable.connection_error
                }
                else -> {
                    R.drawable.connection_error
                }
            }

            Glide
                .with(placeHolderImage)
                .load(image)
                .placeholder(R.drawable.ic_free_icon_font_cross)
                .apply(RequestOptions().override(240, 240))
                .into(placeHolderImage)

            placeHolderMessage.text = text
            placeHolder.visibility = View.VISIBLE
            trackAdapter.tracks.clear()
            trackAdapter.notifyDataSetChanged()

            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeHolder.visibility = View.GONE
        }
    }


}