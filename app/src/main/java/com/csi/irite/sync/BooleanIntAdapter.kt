package com.csi.irite.sync

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.reflect.Type

class BooleanIntAdapter : TypeAdapter<Boolean>() {
    override fun write(out: JsonWriter, value: Boolean?) {
        out.value(if (value == true) 1 else 0)
    }

    override fun read(`in`: JsonReader): Boolean {
        return when (`in`.peek()) {
            JsonToken.NUMBER -> `in`.nextInt() == 1
            JsonToken.BOOLEAN -> `in`.nextBoolean()
            JsonToken.NULL -> {
                `in`.nextNull()
                false
            }
            else -> throw JsonParseException("Unexpected token when parsing Boolean")
        }
    }
}