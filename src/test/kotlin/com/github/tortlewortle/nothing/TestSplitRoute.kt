package com.github.tortlewortle.nothing

import org.junit.Test
import kotlin.test.assertEquals

class TestSplitRoute() {

    @Test
    fun splitsCorrectly() {
        val res = splitRoute("hello world", "value", false)
        assertEquals(2, res.size)
        assertEquals(null, res[0].value)
        assertEquals("value", res.last().value)
    }

    @Test
    fun splitsParams() {
        val res = splitRoute("hello :world", "value", false)
        assertEquals(2, res.size)
        assertEquals(null, res[0].value)
        assertEquals("value", res[1].value)
        assertEquals(true, res[0].static)
        assertEquals(false, res[1].static)
    }

    // TODO: Write more for optionals, only static and concat
}