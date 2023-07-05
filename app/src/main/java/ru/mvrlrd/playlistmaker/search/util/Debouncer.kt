package ru.mvrlrd.playlistmaker.search.util

import androidx.lifecycle.LifecycleCoroutineScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Debouncer {
    private var isClickAllowed = true

    fun playClickDebounce(button: FloatingActionButton, scope: LifecycleCoroutineScope): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            button.isEnabled = false
            scope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
                button.isEnabled = true
            }
        }
        return current
    }
    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 200L
    }
}