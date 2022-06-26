package com.abroadbent.jackson.dsl

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.DoubleNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.LongNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.databind.node.ValueNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val mapper = jacksonObjectMapper()

@DslMarker
annotation class JsonMarker

@JsonMarker
abstract class JacksonElement(val node: JsonNode) {

    override fun hashCode(): Int = node.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JacksonElement) return false
        return node == other.node
    }

    override fun toString(): String = mapper.writeValueAsString(node)
}

class JacksonPrimitive(node: ValueNode) : JacksonElement(node)

class JacksonObject(node: ObjectNode = mapper.createObjectNode()) : JacksonElement(node) {

    fun `object`(key: String, value: JacksonObject.() -> Unit): JacksonObject = put(key, JacksonObject().apply(value).node)

    fun obj(key: String, value: JacksonObject.() -> Unit): JacksonObject = `object`(key, value)

    fun array(key: String, value: JacksonArray.() -> Unit): JacksonObject = put(key, JacksonArray().apply(value).node)
    fun arr(key: String, value: JacksonArray.() -> Unit): JacksonObject = array(key, value)

    fun put(key: String, value: String): JacksonObject = put(key, TextNode(value))
    fun put(key: String, value: Int): JacksonObject = put(key, IntNode(value))
    fun put(key: String, value: Long): JacksonObject = put(key, LongNode(value))
    fun put(key: String, value: Double): JacksonObject = put(key, DoubleNode(value))
    fun put(key: String, value: Boolean): JacksonObject = put(key, BooleanNode.valueOf(value))
    fun put(key: String, value: JacksonElement): JacksonObject = put(key, value.node)

    private fun put(key: String, value: JsonNode) = (node as ObjectNode).replace(key, value).let { this }
}

class JacksonArray(array: ArrayNode = mapper.createArrayNode()) : JacksonElement(array) {

    fun `object`(value: JacksonObject.() -> Unit): JacksonArray = add(JacksonObject().apply(value).node)
    fun obj(value: JacksonObject.() -> Unit): JacksonArray = `object`(value)

    fun array(value: JacksonArray.() -> Unit): JacksonArray = add(JacksonArray().apply(value).node)
    fun arr(value: JacksonArray.() -> Unit): JacksonArray = array(value)

    fun add(value: String): JacksonArray = add(TextNode(value))
    fun add(value: Int): JacksonArray = add(IntNode(value))
    fun add(value: Long): JacksonArray = add(LongNode(value))
    fun add(value: Double): JacksonArray = add(DoubleNode(value))
    fun add(value: Boolean): JacksonArray = add(BooleanNode.valueOf(value))
    fun add(value: JacksonElement): JacksonArray = add(value.node)

    private fun add(node: JsonNode) = (this.node as ArrayNode).add(node).let { this }
}

fun `object`(init: JacksonObject.() -> Unit): JacksonObject = JacksonObject().apply(init)
fun obj(init: JacksonObject.() -> Unit): JacksonObject = `object`(init)

fun array(init: JacksonArray.() -> Unit): JacksonArray = JacksonArray().apply(init)
fun arr(init: JacksonArray.() -> Unit): JacksonArray = array(init)

fun string(value: String) = JacksonPrimitive(TextNode(value))
fun int(value: Int) = JacksonPrimitive(IntNode(value))
fun long(value: Long) = JacksonPrimitive(LongNode(value))
fun double(value: Double) = JacksonPrimitive(DoubleNode(value))
fun boolean(value: Boolean) = JacksonPrimitive(BooleanNode.valueOf(value))
