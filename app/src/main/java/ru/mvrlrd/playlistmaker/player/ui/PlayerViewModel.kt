package ru.mvrlrd.playlistmaker.player.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer

class PlayerViewModel(trackForPlayer: TrackForPlayer): ViewModel() {
    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> = _screenState

    init {
        _screenState.value = PlayerScreenState.Preview(trackForPlayer)
    }

//    private val handler: Handler = Handler(Looper.getMainLooper())
//
//    private val timerGo =
//        object : Runnable {
//            override fun run() {
//                updateTimer(playerPresenter.getCurrentPosition())
//                handler.postDelayed(
//                    this,
//                    REFRESH_TIMER_DELAY_MILLIS,
//                )
//            }
//        }

    fun onDestroy() {
//        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun d(){
//        handler.postDelayed(timerGo, PlayerActivity.REFRESH_TIMER_DELAY_MILLIS)
    }


    companion object {

        private const val REFRESH_TIMER_DELAY_MILLIS = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(trackForPlayer: TrackForPlayer): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        trackForPlayer
                    ) as T
                }
            }
    }
}