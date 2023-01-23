package ru.mvrlrd.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.mvrlrd.playlistmaker.model.Track
import ru.mvrlrd.playlistmaker.model.TrackDb
import ru.mvrlrd.playlistmaker.recycler.TrackAdapter

const val INPUT_TEXT = "INPUT_TEXT"
class SearchActivity : AppCompatActivity() {


    var text = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val myApplication: MyApplication = MyApplication()

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }


        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val clearIcon = findViewById<ImageButton>(R.id.clearTextButton)

        if (savedInstanceState!=null){
            searchEditText.setText(savedInstanceState.getString(INPUT_TEXT))
        }

        clearIcon.setOnClickListener {
            searchEditText.text.clear()
        }
        val trackAdapter = TrackAdapter()
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
                        trackAdapter.tracks = myApplication.trackDb.allTracks.filter { track ->
                            track.artistName.startsWith(
                                p0.toString(),
                                ignoreCase = true
                            )
                        } as MutableList<Track>
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
}