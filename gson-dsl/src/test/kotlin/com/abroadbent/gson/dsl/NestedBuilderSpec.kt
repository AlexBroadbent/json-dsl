package com.abroadbent.gson.dsl

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.kotest.matchers.shouldBe

class NestedBuilderSpec : BaseSpec({
    should("build mixed array") {
        val json = array {
            add("foo")
            array {
                add("bar")
                `object` {
                    put("two", "three")
                }
            }
        }

        val expected = JsonArray()
            .apply { add("foo") }
            .apply {
                add(
                    JsonArray()
                        .apply { add("bar") }
                        .apply {
                            add(
                                JsonObject().apply { addProperty("two", "three") }
                            )
                        }
                )
            }

        json shouldBe GsonArray(expected)
    }

    should("build mixed object") {
        val json = `object` {
            put("one", 987)
            `object`("two") {
                put("three", false)
                array("four") {
                    add("foobar")
                }
            }
        }

        val expected = JsonObject()
            .apply { addProperty("one", 987) }
            .apply {
                add(
                    "two",
                    JsonObject()
                        .apply { addProperty("three", false) }
                        .apply { add("four", JsonArray().apply { add("foobar") }) }
                )
            }

        json shouldBe GsonObject(expected)
    }

    should("build with shortened names object") {
        val json = obj {
            put("one", 213)
            arr("two") {
                add(false)
                obj {
                    put("three", "bar")
                }
            }
            obj("four") {
                put("five", 3.14)
            }
        }

        val expected = JsonObject()
            .apply { addProperty("one", 213) }
            .apply {
                add(
                    "two",
                    JsonArray()
                        .apply { add(false) }
                        .apply {
                            add(
                                JsonObject()
                                    .apply { addProperty("three", "bar") }
                            )
                        }
                )
            }
            .apply { add("four", JsonObject().apply { addProperty("five", 3.14) }) }

        json shouldBe GsonObject(expected)
    }

    should("build with shortened names array") {
        val json = arr {
            add(213)
            arr {
                add(false)
            }
            obj {
                put("one", 3.14)
            }
        }

        val expected = JsonArray()
            .apply { add(213) }
            .apply { add(JsonArray().apply { add(false) }) }
            .apply { add(JsonObject().apply { addProperty("one", 3.14) }) }

        json shouldBe GsonArray(expected)
    }
})
