package ru.mvrlrd.playlistmaker.tools

import java.util.*

fun addSuffix(count: Int): String {
    val language = Locale.getDefault().language
    return when (count) {
        1 -> {
            ""
        }
        2, 3, 4 -> {
            if (language == "ru") {
                "а"
            } else {
                "s"
            }
        }
        else -> {
            if (language == "ru") {
                "ов"
            } else {
                "s"
            }
        }
    }
}

