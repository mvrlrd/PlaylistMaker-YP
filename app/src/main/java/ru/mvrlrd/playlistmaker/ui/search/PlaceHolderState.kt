package ru.mvrlrd.playlistmaker.ui.search

sealed class PlaceHolderState(val message: String? = null ){
    class Loading : PlaceHolderState()
    class NothingFound : PlaceHolderState()
    class Error(message: String?) : PlaceHolderState(message)
    class Empty: PlaceHolderState()


}
