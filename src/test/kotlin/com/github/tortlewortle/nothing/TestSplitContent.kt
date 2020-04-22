package com.github.tortlewortle.nothing

import org.junit.Test

class TestSplitContent() {
    private val testCases: Map<String, Array<String>> = mapOf(
        "This is a message." to arrayOf("This", "is", "a", "message."),
        ";todo add \"my todo message\"" to arrayOf(";todo", "add", "my todo message"),
        ";my broken \"message" to arrayOf(";my", "broken", "\"message"),
        ";todo add \"my todo \"message\"" to arrayOf(";todo", "add", "my todo \"message"),
        "Two  spaces" to arrayOf("Two", "spaces"),
        "\"Two  spaces in quotes\"" to arrayOf("Two  spaces in quotes")
    )

    @Test
    fun normal() {
        testCases.forEach { (string, expected) ->
            val result = splitContent(string)

            assert(result.contentDeepEquals(expected)) {
                println(String.format("EXPECTED: (%d)", expected.size))
                println(expected.joinToString(", "))
                println(String.format("GOT: (%d)", result.size))
                println(result.joinToString(", "))
            }
        }
    }
}
