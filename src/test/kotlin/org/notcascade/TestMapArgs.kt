package org.notcascade

import org.junit.Test
import org.notcascade.core.commands.mapArgs
import kotlin.test.assertEquals

class TestMapArgs {

    @Test fun TestMapMultipleArgs() {
        val route = "tell :recipient :message"
        val rawInput = "tell john \"hello there\""

        val out = mapArgs(rawInput, route)
        assertEquals("hello there", out["message"])
        assertEquals("john", out["recipient"])
    }

    @Test fun TestMapMultipleMixedArgs() {
        val route = "tell :recipient *message"
        val rawInput = "tell john hello there"

        val out = mapArgs(rawInput, route)
        assertEquals("hello there", out["message"])
        assertEquals("john", out["recipient"])
    }

    @Test fun TestMapArgs() {
        val route = "say :message"
        val rawInput = "say \"hello there\""

        val out = mapArgs(rawInput, route)
        assertEquals("hello there", out["message"])
    }

    @Test fun TestMapArgsConcat() {
        val route = "say *message"
        val rawInput = "say hello there"

        val out = mapArgs(rawInput, route)
        assertEquals("hello there", out["message"])
    }

    @Test fun TestMapArgsConcatMultipleSpaces() {
        val route = "say *message"
        val rawInput = "say \"hello  there\""

        val out = mapArgs(rawInput, route)
        assertEquals("hello  there", out["message"])
    }
}
