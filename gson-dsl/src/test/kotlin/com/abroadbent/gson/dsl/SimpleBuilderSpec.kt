package com.abroadbent.gson.dsl

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.kotest.matchers.shouldBe

class SimpleBuilderSpec : BaseSpec({
    should("build array") {
        val json = array {
            add("foo")
            add(3)
            add(124789124768219L)
            add(3.14)
            add(false)
        }

        val expected = JsonArray()
            .apply { add("foo") }
            .apply { add(3) }
            .apply { add(124789124768219L) }
            .apply { add(3.14) }
            .apply { add(false) }

        json shouldBe GsonArray(expected)
    }

    should("build object") {
        val json = `object` {
            put("string", "foobar")
            put("int", 123)
            put("long", 718492718496124L)
            put("double", 3.14)
            put("boolean", true)
        }

        val expected = JsonObject()
            .apply { addProperty("string", "foobar") }
            .apply { addProperty("int", 123) }
            .apply { addProperty("long", 718492718496124L) }
            .apply { addProperty("double", 3.14) }
            .apply { addProperty("boolean", true) }

        json shouldBe GsonObject(expected)
    }

    should("equate array toString values") {
        val json = array {
            add("foo")
            add(472)
            add(false)
        }

        json.toString() shouldBe """["foo",472,false]"""
    }

    should("equate object toString values") {
        val json = `object` {
            put("foo", "bar")
            put("ham", 472)
            put("spam", false)
        }

        json.toString() shouldBe """{"foo":"bar","ham":472,"spam":false}"""
    }

    should("return allow chained operations") {
        val json = obj {}.put("foo", "bar").arr("egg") { add(123).add(456).add(789) }

        val expected = JsonObject()
            .apply { addProperty("foo", "bar") }
            .apply {
                add("egg", JsonArray()
                    .apply { add(123) }
                    .apply { add(456) }
                    .apply { add(789) }
                )
            }

        json shouldBe GsonObject(expected)
    }
})
