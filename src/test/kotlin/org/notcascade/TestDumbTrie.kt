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
    }

    @Test
    fun works() {
        assertEquals("Help Command", root.find("help"))
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
}
