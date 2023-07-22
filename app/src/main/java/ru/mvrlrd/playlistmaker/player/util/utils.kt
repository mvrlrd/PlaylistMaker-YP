package ru.mvrlrd.playlistmaker.player.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

fun unparseDateToYear(input: String?): String {
    return try {
        val instant: Instant = Instant.parse(input)
        val odt: OffsetDateTime = instant.atOffset(ZoneOffset.UTC)
        val f: DateTimeFormatter = DateTimeFormatter.ofPattern(YEAR_FORMAT)
        odt.format(f)
    } catch (e: Exception) {
        UNDEFINED_TEXT_FIELD
    }
}

fun formatTime(currentTime: Int): String {
    return SimpleDateFormat(TIMER_FORMAT, Locale.getDefault()).format(currentTime).toString()
}

private const val YEAR_FORMAT = "uuuu"
private const val UNDEFINED_TEXT_FIELD = "???"
private const val TIMER_FORMAT = "mm:ss"



