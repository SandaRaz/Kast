package com.ssw.kast.model.serializer

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.threeten.bp.LocalDateTime

// LocalDateTime to DateTime json(gson) adapter
class LDTtoDT : TypeAdapter<LocalDateTime>() {
    override fun write(out: JsonWriter?, value: LocalDateTime?) {
        out?.value(DateUtils.localDateTimeToCsharpDateTime(value))
    }

    override fun read(`in`: JsonReader?): LocalDateTime {
        val dateTimeString = `in`?.nextString()!!
        val localDateTime = DateUtils.csharpDateTimeToLocalDateTime(dateTimeString)

        return localDateTime
    }
}