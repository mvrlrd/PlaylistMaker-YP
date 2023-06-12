package ru.mvrlrd.playlistmaker.search.domain


interface TracksInteractor {
    fun searchTracks(query: String, consumer: TracksConsumer)
    interface TracksConsumer{
        fun consume(foundTracks: List<Track>?, responseCode: Int, message: String? = null)
    }

    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}