package org.notcascade

import org.junit.Test
import org.notcascade.core.commands.Node
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TestDumbTrie {
    private val root = Node<String>("rootNode", true)

    init {
        root.add("help", "Help Command")
        root.add("help :page", "Help Command with page")
        root.add("help :page edit", "Help Command with page and subcommand")
        root.add("play *query", "play command with query")
        root.add("tell :recipient *message", "mixed arg type command")

        root.add(":mytag", "tag with colon", onlyStatic = true)

        root.add("*mytag", "tag with star", onlyStatic = true)

        root.add("page :number?", "page optional param")

        root.add("yell *message?", "page optional concat")

        root.add("todo :number? edit :other?", "edit optional params")

        root.add("kick :person? *reason?", "double optionals")
    }

    @Test
    fun works() {
        assertEquals("Help Command", root.get("help"))
        assertEquals("Help Command", root.get("hElp"))
        assertEquals("Help Command with page", root.get("help 1"))
        assertEquals("Help Command with page and subcommand", root.get("help 1 edit"))
        assertEquals("play command with query", root.get("play rick astley"))
        assertEquals(null, root.get("Nothing"))
        assertEquals("mixed arg type command", root.get("tell john I love refrigerators"))
        assertEquals("mixed arg type command", root.get("tell john hello"))

        assertEquals("mixed arg type command", root.get("tell :recipient *message", ignoreParams = true))
        assertNotEquals("mixed arg type command", root.get("tell :recipent *message", ignoreParams = true))

        assertEquals("tag with colon", root.get(":mytag"))
        assertEquals("tag with star", root.get("*mytag"))
    }


    @Test
    fun optional() {
        assertEquals("page optional param", root.get("page"))
        assertEquals("page optional param", root.get("page one"))

        assertNotEquals("edit optional params", root.get("todo 5"))

        assertEquals("edit optional params", root.get("todo 5 edit"))
        assertEquals("edit optional params", root.get("todo 5 edit 60"))

        assertEquals("page optional concat", root.get("yell Hello there friends"))
        assertEquals("page optional concat", root.get("yell "))

        assertEquals("double optionals", root.get("kick"))
        assertEquals("double optionals", root.get("kick person"))
        assertEquals("double optionals", root.get("kick person reason"))
    }

    @Test
    fun doubleOptional() {
        val root = Node<String>("rootNode", true)
        root.add("todo :id edit", "todo :id edit")
        root.add("todo :id :action", "todo :id :action")

        assertEquals("todo :id edit", root.get("todo 1 edit"))
        assertEquals("todo :id :action", root.get("todo 2 remove"))
    }

    @Test
    fun test() {
        val root = Node<String>("rootNode", true)
        root.add("todo :id edit", "edit :id")
        root.add("todo :id test test", "edit :id test test")

        assertEquals("edit :id", root.get("todo 1 edit"))
        assertEquals("edit :id test test", root.get("todo 1 test test"))
    }

    @Test
    fun testEscaping() {
        val root = Node<String>("rootNode", true)
        root.add("todo \\:id edit", "edit :id")

        assertEquals("edit :id", root.get("todo :id edit"))
        assertNotEquals("edit :id", root.get("todo \\:id edit"))
        assertNotEquals("edit :id", root.get("todo 123 edit"))
    }

    @Test
    fun canReplaceRouteValue() {
        val root = Node<String>("rootNode", true)
        root.add("first", "default")
        assertEquals("default", root.get("first"))
        root.set("first", "changed")
        assertEquals("changed", root.get("first"))

        root.set("second", "default")
        assertEquals("default", root.get("second"))
        root.set("second", "changed")
        assertEquals("changed", root.get("second"))
    }

    @Test
    fun canDeleteRoute() {
        val root = Node<String>("rootNode", true)
        root.set("first", "default")
        assertEquals("default", root.get("first"))
        root.remove("first")
        assertEquals(null, root.get("first"))
    }

    @Test
    fun testCount() {
        val root = Node<String>("rootNode", true)
        assertEquals(0, root.count())
        root.set("first", "default")
        assertEquals(1, root.count())
        root.set("second", "default")
        root.set("third", "default")
        assertEquals(3, root.count())
        root.remove("first")
        assertEquals(2, root.count())
    }

    @Test
    fun testSetAddsIfNotFound() {
        val root = Node<String>("rootNode", true)
        root.add("first", "default")
        val root2 = Node<String>("rootNode", true)
        root2.set("first", "default")
    }
}
