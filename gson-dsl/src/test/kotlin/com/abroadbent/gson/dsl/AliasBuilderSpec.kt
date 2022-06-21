package com.abroadbent.gson.dsl

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.kotest.matchers.shouldBe

class AliasBuilderSpec : BaseSpec({

    context("object.put") {
        should("accept String parameter") {
            obj { put("foo", "bar") }.node shouldBe JsonObject().apply { addProperty("foo", "bar") }
        }

        should("accept Int parameter") {
            obj { put("foo", 123) }.node shouldBe JsonObject().apply { addProperty("foo", 123) }
        }

        should("accept Long parameter") {
            obj { put("foo", 567L) }.node shouldBe JsonObject().apply { addProperty("foo", 567L) }
        }

        should("accept Double parameter") {
            obj { put("foo", 8.901) }.node shouldBe JsonObject().apply { addProperty("foo", 8.901) }
        }

        should("accept Boolean parameter") {
            obj { put("foo", false) }.node shouldBe JsonObject().apply { addProperty("foo", false) }
        }
    }

    context("array.add") {
        should("accept String parameter") {
            arr { add("foo") }.node shouldBe JsonArray().apply { add("foo") }
        }

        should("accept Int parameter") {
            arr { add(123) }.node shouldBe JsonArray().apply { add(123) }
        }

        should("accept Long parameter") {
            arr { add(567L) }.node shouldBe JsonArray().apply { add(567L) }
        }

        should("accept Double parameter") {
            arr { add(8.901) }.node shouldBe JsonArray().apply { add(8.901) }
        }

        should("accept Boolean parameter") {
            arr { add(false) }.node shouldBe JsonArray().apply { add(false) }
        }
    }
})
