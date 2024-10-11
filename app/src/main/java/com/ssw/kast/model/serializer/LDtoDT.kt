package com.ssw.kast.model.serializer

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.threeten.bp.LocalDate

// LocalDate to DateTime json(gson) adapter
class LDtoDT : TypeAdapter<LocalDate>() {
    override fun write(out: JsonWriter?, value: LocalDate?) {
        out?.value(DateUtils.localDateToCsharpDateTime(value))
    }

    override fun read(`in`: JsonReader?): LocalDate {
        val dateTimeString = `in`?.nextString()!!
        val localDate: LocalDate = DateUtils.csharpDateTimeToLocalDate(dateTimeString)

        return localDate
    }
}