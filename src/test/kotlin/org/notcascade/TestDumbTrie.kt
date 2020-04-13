package org.notcascade

import org.junit.Test
import org.notcascade.core.commands.Node
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TestDumbTrie {
    private val root = Node<String>("rootNode", true)

    init {
        root.addRoute("help", "Help Command")
        root.addRoute("help :page", "Help Command with page")
        root.addRoute("help :page edit", "Help Command with page and subcommand")
        root.addRoute("play *query", "play command with query")
        root.addRoute("tell :recipient *message", "mixed arg type command")

        root.addRoute(":mytag", "tag with colon", onlyStatic = true)

        root.addRoute("*mytag", "tag with star", onlyStatic = true)

        root.addRoute("page :number?", "page optional param")

        root.addRoute("yell *message?", "page optional concat")

        root.addRoute("todo :number? edit :other?", "edit optional params")

        root.addRoute("kick :person? *reason?", "double optionals")
    }

    @Test
    fun works() {
        assertEquals("Help Command", root.find("help"))
        assertEquals("Help Command", root.find("hElp"))
        assertEquals("Help Command with page", root.find("help 1"))
        assertEquals("Help Command with page and subcommand", root.find("help 1 edit"))
        assertEquals("play command with query", root.find("play rick astley"))
        assertEquals(null, root.find("Nothing"))
        assertEquals("mixed arg type command", root.find("tell john I love refrigerators"))

        assertEquals("mixed arg type command", root.find("tell :recipient *message", ignoreParams = true))
        assertNotEquals("mixed arg type command", root.find("tell :recipent *message", ignoreParams = true))

        assertEquals("tag with colon", root.find(":mytag"))
        assertEquals("tag with star", root.find("*mytag"))
    }


    @Test
    fun optional() {
        assertEquals("page optional param", root.find("page"))
        assertEquals("page optional param", root.find("page one"))

        assertNotEquals("edit optional params", root.find("todo 5"))

        assertEquals("edit optional params", root.find("todo 5 edit"))
        assertEquals("edit optional params", root.find("todo 5 edit 60"))

        assertEquals("page optional concat", root.find("yell Hello there friends"))
        assertEquals("page optional concat", root.find("yell "))

        assertEquals("double optionals", root.find("kick"))
        assertEquals("double optionals", root.find("kick person"))
        assertEquals("double optionals", root.find("kick person reason"))
    }

    @Test
    fun doubleOptional() {
        val root = Node<String>("rootNode", true)
        root.addRoute("todo :id edit", "todo :id edit")
        root.addRoute("todo :id :action", "todo :id :action")

        assertEquals("todo :id edit", root.find("todo 1 edit"))
        assertEquals("todo :id :action", root.find("todo 2 remove"))
    }
}
