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

class GsonObject(node: JsonObject = JsonObject()) : GsonElement(node) {

    fun `object`(key: String, value: GsonObject.() -> Unit) = set(key, GsonObject().apply(value).node)
    fun obj(key: String, value: GsonObject.() -> Unit) = `object`(key, value)

    fun array(key: String, value: GsonArray.() -> Unit) = set(key, GsonArray().apply(value).node)
    fun arr(key: String, value: GsonArray.() -> Unit) = array(key, value)

    fun put(key: String, value: String) = set(key, JsonPrimitive(value))
    fun put(key: String, value: Int) = set(key, JsonPrimitive(value))
    fun put(key: String, value: Long) = set(key, JsonPrimitive(value))
    fun put(key: String, value: Double) = set(key, JsonPrimitive(value))
    fun put(key: String, value: Boolean) = set(key, JsonPrimitive(value))

    private fun set(key: String, value: JsonElement) = (node as JsonObject).add(key, value)
}

class GsonArray(node: JsonArray = JsonArray()) : GsonElement(node) {
    fun `object`(value: GsonObject.() -> Unit) = add(GsonObject().apply(value).node)
    fun obj(value: GsonObject.() -> Unit) = `object`(value)

    fun array(value: GsonArray.() -> Unit) = add(GsonArray().apply(value).node)
    fun arr(value: GsonArray.() -> Unit) = array(value)

    fun add(value: String) = add(JsonPrimitive(value))
    fun add(value: Int) = add(JsonPrimitive(value))
    fun add(value: Long) = add(JsonPrimitive(value))
    fun add(value: Double) = add(JsonPrimitive(value))
    fun add(value: Boolean) = add(JsonPrimitive(value))

    private fun add(node: JsonElement) {
        (this.node as JsonArray).add(node)
    }
}

fun `object`(init: GsonObject.() -> Unit) = GsonObject().apply(init)
fun obj(init: GsonObject.() -> Unit) = `object`(init)

fun array(init: GsonArray.() -> Unit) = GsonArray().apply(init)
fun arr(init: GsonArray.() -> Unit) = array(init)
