package com.abroadbent.jackson.dsl

import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.DoubleNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.LongNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe

class PrimitiveBuilderSpec : BaseSpec({

    context("create root node") {
        should("create String type") {
            string("hello world!") shouldBe JacksonPrimitive(mapper.readValue<TextNode>(""""hello world!""""))
        }

        should("create Int type") {
            int(123) shouldBe JacksonPrimitive(mapper.readValue<IntNode>("123"))
        }

        should("create Long type") {
            long(674864721964712L) shouldBe JacksonPrimitive(mapper.readValue<LongNode>("674864721964712"))
        }

        should("create Double type") {
            double(3.14) shouldBe JacksonPrimitive(mapper.readValue<DoubleNode>("3.14"))
        }

        should("create Boolean type") {
            boolean(true) shouldBe JacksonPrimitive(mapper.readValue<BooleanNode>("true"))
        }
    }

    context("object") {
        should("add primitive to object") {
            val node = string("bar")
            val json = obj { put("foo", node) }

            json shouldBe JacksonObject(mapper.createObjectNode().put("foo", "bar"))
        }
    }

    context("array") {
        should("add primitive to array") {
            val node = int(123)
            val json = arr { add(node) }

            json shouldBe JacksonArray(mapper.createArrayNode().add(123))
        }
    }
})
