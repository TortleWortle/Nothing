package com.github.tortlewortle.nothing

import org.junit.Test

class TestRouteValidation {
    @Test(expected = InvalidRouteException::class)
    fun failsMultiple() {
        validateRoute("*hello *yeet")
    }

    @Test(expected = InvalidRouteException::class)
    fun failsNotAtEnd() {
        validateRoute("*hello yeet")
    }

    @Test(expected = InvalidRouteException::class)
    fun failsDuplicate() {
        validateRoute("tell :message *message")
    }

    @Test()
    fun validRoutes() {
        validateRoute("hello *message")
        validateRoute("hello :message *recipient")
        validateRoute("hello :message :recipient")
        validateRoute(":yeet")
    }
}
