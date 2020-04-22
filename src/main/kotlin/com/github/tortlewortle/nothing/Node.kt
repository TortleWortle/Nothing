package com.github.tortlewortle.nothing

open class Node<T>(
    private val key: String = "RootNode",
    val static: Boolean = false,
    var value: T? = null,
    val optional: Boolean = false
) {
    private val children = ArrayList<Node<T>>()

    fun add(route: String, value: T?, onlyStatic: Boolean = false) {
        val parts : MutableList<Node<T>> =
            splitRoute(route, value, onlyStatic)
        insertNodes(parts)
    }

    fun get(route: String, ignoreParams: Boolean = false): T? = getNode(route, ignoreParams).value

    fun set(route: String, value: T?, onlyStatic: Boolean = false) {
        val node = getNode(route, ignoreParams = true)
        if (node.value == null) {
            return add(route, value, onlyStatic)
        }

        node.value = value
    }

    fun count(): Int {
        val count = children.count {
            it.value != null
        }
        return children.fold(count) { acc, node ->
            val new = acc + node.count()
            new
        }
    }

    fun remove(route: String) {
        set(route, null, onlyStatic = true)
        return cleanup()
    }

    private fun isDeadEnd(): Boolean {
        return value == null && children.all {
            it.value == null && it.isDeadEnd()
        }
    }

    private fun cleanup() {
        children.removeAll {
            it.isDeadEnd()
        }
    }

    fun getNode(route: String, ignoreParams: Boolean = false): Node<T> {
        val parts = splitContent(route).toMutableList()
        return getNode(parts, ignoreParams)
    }

    private fun hasOnlyOptionalChildren(): Boolean {
        return children.all {
            it.optional && it.hasOnlyOptionalChildren()
        }
    }

    private fun optionalChild(): Node<T> {
        if (children.isNotEmpty()) {
            val child = children.find {
                it.optional && it.hasOnlyOptionalChildren()
            }
            if (child != null) {
                return child.optionalChild()
            }
        }
        return this
    }

    private fun getNode(parts: MutableList<String>, ignoreParams: Boolean = false): Node<T> {
        val firstPart = parts.first()
        parts.removeAt(0)

        var foundNode = children.find {
            it.key.equals(firstPart, ignoreCase = true)
        }

        if (foundNode == null && !ignoreParams) {
            foundNode = children.find {
                !it.static
            }
        }

        if (foundNode != null) {
            return if (parts.size > 0) {
                foundNode.getNode(parts, ignoreParams)
            } else {
                val optionalChild = foundNode.optionalChild()
                optionalChild
            }
        }
        return this
    }

    private fun insertNodes(nodes: MutableList<Node<T>>) {
        val node = nodes.first()
        nodes.removeAt(0)


        val foundNode = children.find {
            it.key == node.key
        }

        if (foundNode != null) {
            if (nodes.size > 0) {
                foundNode.insertNodes(nodes)
            }
        } else {
            if (nodes.size > 0) {
                node.insertNodes(nodes)
            }
            children.add(node)
        }

    }
}


fun <T> splitRoute(route: String, value: T?, onlyStatic: Boolean = false): MutableList<Node<T>> {
    val ret = ArrayList<Node<T>>()
    for (it in route.split(" ")) {
        val escaped = it.startsWith("\\")
        val key = if (escaped) it.removePrefix("\\") else it

        if (key.startsWith("*") && !escaped) {
            ret.add(Node(key, onlyStatic, null, key.endsWith("?")))
            break
        }
        if (key.startsWith(":") && !escaped) {
            ret.add(Node(key, onlyStatic, null, key.endsWith("?")))
        } else {
            ret.add(Node(key, true, null))
        }
    }
    ret.last().value = value

    return ret
}
