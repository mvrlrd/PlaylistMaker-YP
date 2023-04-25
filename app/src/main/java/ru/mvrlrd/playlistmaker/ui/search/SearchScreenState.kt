package ru.mvrlrd.playlistmaker.ui.search

import ru.mvrlrd.playlistmaker.domain.Track

sealed class SearchScreenState(val message: String? = null, val tracks: List<Track>? = null)
//    : Refresh
{
    class Loading : SearchScreenState(){
//        override fun render() {
//            TODO("Not yet implemented")
//        }
    }
    class NothingFound : SearchScreenState()
    class Error(message: String?) : SearchScreenState(message = message)
    class Success(tracks: List<Track>?): SearchScreenState(tracks = tracks)
    class ShowHistory(tracks: List<Track>?): SearchScreenState(tracks = tracks)
}
//interface Refresh{
//    fun render()
//}
