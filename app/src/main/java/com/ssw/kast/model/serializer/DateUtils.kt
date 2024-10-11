package com.ssw.kast.model.serializer

import android.util.Log
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

class DateUtils {
    companion object {
        fun csharpDateTimeToLocalDateTime(dateTimeString: String): LocalDateTime {
            var dts = dateTimeString
            val conformLength = "0001-01-01T00:00:00".length
            if (dts.length > conformLength) {
                dts = dts.substring(0, conformLength)
            }

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val localDateTime = LocalDateTime.parse(dts, formatter)

            return localDateTime
        }

        fun csharpDateTimeToLocalDate(dateTimeString: String): LocalDate {
            var dts = dateTimeString
            val conformLength = "0001-01-01T00:00:00".length
            if (dts.length > conformLength) {
                dts = dts.substring(0, conformLength)
            }

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val localDate = LocalDateTime.parse(dts, formatter).toLocalDate()

            return localDate
        }

        fun csharpTimeSpanToDuration(durationString: String): Duration {
            //Log.d("DateUtils", "Reading duration: $durationString")

            val timeParts = durationString.split(":")
            //Log.d("DataUtils", "TimeParts: $timeParts")
            val hours = timeParts[0].toLong()
            val minutes = timeParts[1].toLong()

            val secondsAndFraction = timeParts[2].split(".")
            val seconds = secondsAndFraction[0].toLong()

            val nanoseconds = if (secondsAndFraction.size > 1) {
                secondsAndFraction[1].toLong()
            } else {
                0L
            }

            val duration = Duration.ofSeconds(hours * 3600 + minutes * 60 + seconds) + Duration.ofNanos(nanoseconds)

            //Log.d("DurationTypeAdapter", "Test Duration String ===> h=$hours, m=$minutes, s=$seconds, n=$nanoseconds")
            //Log.d("DurationTypeAdapter", "Return duration: $duration")

            return duration
        }

        fun durationToCsharpTimeSpan(duration: Duration): String {
            val hours = duration.toHoursPart()
            val minutes = duration.toMinutesPart()
            val seconds = duration.toSecondsPart()
            val nanos = duration.nano

            return String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d.%07d",
                hours,
                minutes,
                seconds,
                nanos
            )
        }

        fun durationToString(duration: Duration): String {
            val hours = duration.toHoursPart()
            val minutes = duration.toMinutesPart()
            val seconds = duration.toSecondsPart()
            
            var durationString = String.format(
                Locale.getDefault(),
                "%02d:%02d",
                minutes,
                seconds,
            )

            if (hours > 0) {
                durationString = String.format(
                    Locale.getDefault(),
                    "%02d:%02d:%02d",
                    hours,
                    minutes,
                    seconds
                )
            }
            return durationString
        }

        fun formatTime(timeMillis: Long): String {
            val minutes = (timeMillis / 1000) / 60
            val seconds = (timeMillis / 1000) % 60

            return if (minutes < 0 || seconds < 0) {
                "00:00"
            } else {
                String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    minutes,
                    seconds
                )
            }
        }

        fun localDateTimeToCsharpDateTime(localDateTime: LocalDateTime?): String {
            var ldt = localDateTime
            if (ldt == null) {
                ldt = LocalDateTime.MIN
            }

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val ldtString = ldt!!.format(formatter)

            return ldtString
        }

        fun localDateToCsharpDateTime(localDate: LocalDate?): String {
            var ldt = localDate
            if (ldt == null) {
                ldt = LocalDate.MIN
            }
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val dateString = "${ldt!!.format(formatter)}T00:00:00"

            return dateString
        }


    }
}