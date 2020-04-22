package com.github.tortlewortle.nothing

import org.junit.Test
import kotlin.test.assertEquals

class TestMapArgs {

    @Test
    fun TestMapMultipleArgs() {
        val route = "tell :recipient :message"
        val rawInput = "tell john \"hello there\""

        val out = mapArgs(rawInput, route)
        assertEquals("hello there", out["message"])
        assertEquals("john", out["recipient"])
    }

    @Test
    fun TestMapMultipleMixedArgs() {
        val route = "tell :recipient *message"
        val rawInput = "tell john hello there"

        val out = mapArgs(rawInput, route)
        assertEquals("hello there", out["message"])
        assertEquals("john", out["recipient"])
    }

    @Test
    fun TestMapArgs() {
        val route = "say :message"
        val rawInput = "say \"hello there\""

        val out = mapArgs(rawInput, route)
        assertEquals("hello there", out["message"])
    }

    @Test
    fun TestMapArgsConcat() {
        val route = "say *message"
        val rawInput = "say hello there"

        val out = mapArgs(rawInput, route)
        assertEquals("hello there", out["message"])
    }

    @Test
    fun TestMapArgsConcatMultipleSpaces() {
        val route = "say *message"
        val rawInput = "say \"hello  there\""

        val out = mapArgs(rawInput, route)
        assertEquals("hello  there", out["message"])
    }

    @Test
    fun TestMapArgsOptionalParam() {
        val out = mapArgs("say hai", "say :message?")
        assertEquals(
            "hai",
            out["message"]
        )
    }

    @Test
    fun TestMapArgsOptionalParamEmpty() {
        val out = mapArgs("say", "say :message?")
        assertEquals(
            null,
            out["message"]
        )
    }

    @Test
    fun TestMapArgsOptionalConcat() {
        val out = mapArgs("say hai", "say *message?")
        assertEquals(
            "hai",
            out["message"]
        )
    }

    @Test
    fun TestMapArgsOptionalConcatEmpty() {
        val out = mapArgs("say", "say *message?")
        assertEquals(
            null,
            out["message"]
        )
    }

    @Test
    fun TestMapArgsConcatSingle() {
        val route = "tell :person *message"
        val rawInput = "tell john hello"

        val out = mapArgs(rawInput, route)
        assertEquals("john", out["person"])
        assertEquals("hello", out["message"])
    }
}
