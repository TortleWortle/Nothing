package org.notcascade

import org.junit.Test
import org.notcascade.core.commands.InvalidRouteException
import org.notcascade.core.commands.validateRoute

class TestRouteValidation {
    @Test(expected = InvalidRouteException::class) fun failsMultiple() {
        validateRoute("*hello *yeet")
    }

    @Test(expected = InvalidRouteException::class) fun failsNotAtEnd() {
        validateRoute("*hello yeet")
    }

    @Test(expected = InvalidRouteException::class) fun failsDuplicate() {
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
