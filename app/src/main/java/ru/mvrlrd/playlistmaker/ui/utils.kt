package ru.mvrlrd.playlistmaker

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

private const val TIMER_FORMAT = "mm:ss"
fun unparseDateToYear(input: String): String{
    val instant: Instant = Instant.parse(input)
    val odt: OffsetDateTime = instant.atOffset(ZoneOffset.UTC)
    val f: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu")
    val output = odt.format(f)
    return output
}


fun formatTime(currentTime: Int): String{
    return SimpleDateFormat(TIMER_FORMAT, Locale.getDefault()).format(currentTime).toString()
}




