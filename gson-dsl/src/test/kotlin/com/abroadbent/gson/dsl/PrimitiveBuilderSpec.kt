package com.abroadbent.gson.dsl

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.kotest.matchers.shouldBe

class PrimitiveBuilderSpec : BaseSpec({

    context("create root node") {
        should("create String type") {
            string("hello world!") shouldBe GsonPrimitive(gson.fromJson("\"hello world!\"", JsonPrimitive::class.java))
        }

        should("create Int type") {
            int(123) shouldBe GsonPrimitive(gson.fromJson("123", JsonPrimitive::class.java))
        }

        should("create Long type") {
            long(674864721964712L) shouldBe GsonPrimitive(gson.fromJson("674864721964712", JsonPrimitive::class.java))
        }

        should("create Double type") {
            double(3.14) shouldBe GsonPrimitive(gson.fromJson("3.14", JsonPrimitive::class.java))
        }

        should("create Boolean type") {
            boolean(true) shouldBe GsonPrimitive(gson.fromJson("true", JsonPrimitive::class.java))
        }
    }

    context("object") {
        should("add primitive to object") {
            val node = string("bar")
            val json = obj { put("foo", node) }

            json shouldBe GsonObject(JsonObject().apply { addProperty("foo", "bar") })
        }
    }

    context("array") {
        should("add primitive to array") {
            val node = int(123)
            val json = arr { add(node) }

            json shouldBe GsonArray(JsonArray().apply { add(123) })
        }
    }
})
