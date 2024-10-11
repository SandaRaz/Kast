package com.ssw.kast.model.serializer

import android.util.Log
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.threeten.bp.Duration


// Kotlin org.threeten.bp Duration to Csharp TimeSpan
class DtoTS : TypeAdapter<Duration>() {
    override fun write(out: JsonWriter?, value: Duration?) {
        var durationString = DateUtils.durationToCsharpTimeSpan(Duration.ZERO)
        if (value != null) {
            durationString = DateUtils.durationToCsharpTimeSpan(value)
        }
        out?.value(durationString)
    }

    override fun read(`in`: JsonReader?): Duration {
        val durationString = `in`?.nextString()
        //Log.d("DtoTS", "DurationString: $durationString")

        if (durationString == null) {
            return Duration.ZERO
        } else {
            val duration = DateUtils.csharpTimeSpanToDuration(durationString)
            return duration
        }
    }
}