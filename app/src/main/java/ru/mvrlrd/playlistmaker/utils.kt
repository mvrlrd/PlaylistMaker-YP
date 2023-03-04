package ru.mvrlrd.playlistmaker

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


fun unparseDateToYear(input: String): String{
    val instant: Instant = Instant.parse(input)
    val odt: OffsetDateTime = instant.atOffset(ZoneOffset.UTC)
    val f: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu")
    val output = odt.format(f)
    return output
}