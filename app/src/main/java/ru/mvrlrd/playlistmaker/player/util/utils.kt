package ru.mvrlrd.playlistmaker.player.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

private const val TIMER_FORMAT = "mm:ss"
fun unparseDateToYear(input: String?): String {
    return try {
        val instant: Instant = Instant.parse(input)
        val odt: OffsetDateTime = instant.atOffset(ZoneOffset.UTC)
        val f: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu")
        odt.format(f)
    }catch (e: Exception){
        "???"
    }
}


fun formatTime(currentTime: Int): String{
    return SimpleDateFormat(TIMER_FORMAT, Locale.getDefault()).format(currentTime).toString()
}




