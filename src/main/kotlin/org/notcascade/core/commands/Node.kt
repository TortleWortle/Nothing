package org.notcascade.core.commands

open class Node<T>(
    private val key: String = "RootNode",
    private val static: Boolean = false,
    var value: T? = null,
    private val optional: Boolean = false
) {
    private val children = ArrayList<Node<T>>()

    fun addRoute(route: String, value: T, onlyStatic: Boolean = false) {
        val parts = splitRoute(route, value, onlyStatic)
        insertNodes(parts)
    }

    fun find(route: String, ignoreParams: Boolean = false): T? {
        val parts = splitContent(route).toMutableList()
        return find(parts, ignoreParams)
    }

    private fun hasOnlyOptionalChildren(): Boolean {
        return children.all {
            it.optional && it.hasOnlyOptionalChildren()
        }
    }

    private fun optionalChild(): Node<T> {
        if (children.isNotEmpty()) {
            return children.filter {
                it.optional && it.hasOnlyOptionalChildren()
            }.first().optionalChild()
        }
        return this
    }

    private fun find(parts: MutableList<String>, ignoreParams: Boolean = false): T? {
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
            if (parts.size > 0) {
                return foundNode.find(parts, ignoreParams)
            } else {
                val optionalChild = foundNode.children.find {
                    it.optional && it.hasOnlyOptionalChildren()
                }
                if (optionalChild != null) {
                    return optionalChild.optionalChild().value
                }
            }
            return foundNode.value
        }
        return value
    }

    private fun insertNodes(nodes: ArrayList<Node<T>>) {
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

    private fun splitRoute(route: String, value: T, onlyStatic: Boolean = false): ArrayList<Node<T>> {
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
}
