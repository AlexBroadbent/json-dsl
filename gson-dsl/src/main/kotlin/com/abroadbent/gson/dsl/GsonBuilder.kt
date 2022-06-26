package com.abroadbent.gson.dsl

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

val gson = Gson()

@DslMarker
annotation class JsonMarker

@JsonMarker
abstract class GsonElement(val node: JsonElement) {

    override fun hashCode(): Int = node.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GsonElement) return false
        return node == other.node
    }

    override fun toString(): String = gson.toJson(node)
}

class GsonPrimitive(node: JsonPrimitive) : GsonElement(node)

class GsonObject(node: JsonObject = JsonObject()) : GsonElement(node) {

    fun `object`(key: String, value: GsonObject.() -> Unit): GsonObject = put(key, GsonObject().apply(value).node)
    fun obj(key: String, value: GsonObject.() -> Unit): GsonObject = `object`(key, value)

    fun array(key: String, value: GsonArray.() -> Unit): GsonObject = put(key, GsonArray().apply(value).node)
    fun arr(key: String, value: GsonArray.() -> Unit): GsonObject = array(key, value)

    fun put(key: String, value: String): GsonObject = put(key, JsonPrimitive(value))
    fun put(key: String, value: Int): GsonObject = put(key, JsonPrimitive(value))
    fun put(key: String, value: Long): GsonObject = put(key, JsonPrimitive(value))
    fun put(key: String, value: Double): GsonObject = put(key, JsonPrimitive(value))
    fun put(key: String, value: Boolean): GsonObject = put(key, JsonPrimitive(value))
    fun put(key: String, value: GsonPrimitive): GsonObject = put(key, value.node)

    private fun put(key: String, value: JsonElement) = (node as JsonObject).apply { add(key, value) }.let { this }
}

class GsonArray(node: JsonArray = JsonArray()) : GsonElement(node) {
    fun `object`(value: GsonObject.() -> Unit): GsonArray = add(GsonObject().apply(value).node)
    fun obj(value: GsonObject.() -> Unit): GsonArray = `object`(value)

    fun array(value: GsonArray.() -> Unit): GsonArray = add(GsonArray().apply(value).node)
    fun arr(value: GsonArray.() -> Unit): GsonArray = array(value)

    fun add(value: String): GsonArray = add(JsonPrimitive(value))
    fun add(value: Int): GsonArray = add(JsonPrimitive(value))
    fun add(value: Long): GsonArray = add(JsonPrimitive(value))
    fun add(value: Double): GsonArray = add(JsonPrimitive(value))
    fun add(value: Boolean): GsonArray = add(JsonPrimitive(value))
    fun add(value: GsonPrimitive): GsonArray = add(value.node)

    private fun add(node: JsonElement) = (this.node as JsonArray).apply { add(node) }.let { this }
}

fun `object`(init: GsonObject.() -> Unit) = GsonObject().apply(init)
fun obj(init: GsonObject.() -> Unit) = `object`(init)

fun array(init: GsonArray.() -> Unit) = GsonArray().apply(init)
fun arr(init: GsonArray.() -> Unit) = array(init)

fun string(value: String) = GsonPrimitive(JsonPrimitive(value))
fun int(value: Int) = GsonPrimitive(JsonPrimitive(value))
fun long(value: Long) = GsonPrimitive(JsonPrimitive(value))
fun double(value: Double) = GsonPrimitive(JsonPrimitive(value))
fun boolean(value: Boolean) = GsonPrimitive(JsonPrimitive(value))
