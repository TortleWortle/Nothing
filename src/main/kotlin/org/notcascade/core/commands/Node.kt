package org.notcascade.core.commands

open class Node<T>(val key : String = "RootNode", val static : Boolean = false, var value : T? = null) {
    val children = ArrayList<Node<T>>()

    fun addRoute(route : String, value : T, onlyStatic: Boolean = false) {
        val parts = splitRoute(route, value, onlyStatic)
        insertNodes(parts)
    }

    fun find(route : String, ignoreParams: Boolean = false) : T?{
        val parts = splitContent(route).toMutableList()
        return find(parts, ignoreParams)
    }

    fun find(parts : MutableList<String>, ignoreParams: Boolean = false) : T? {
        val firstPart = parts.first()
        parts.removeAt(0)

        val foundNode = children.find {
            if (ignoreParams)
                it.key.equals(firstPart, ignoreCase = true)
             else
                !it.static || it.key.equals(firstPart, ignoreCase = true)
        }

        if (foundNode != null) {
            if (parts.size > 0) {
                return foundNode.find(parts, ignoreParams)
            }
            return foundNode.value
        }
        return value
    }

    fun insertNodes(nodes : ArrayList<Node<T>>) {
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

    fun splitRoute(route : String, value : T, onlyStatic: Boolean = false) : ArrayList<Node<T>> {
        val ret = ArrayList<Node<T>>()

        for (it in route.split(" ")) {
            if(it.startsWith("*")) {
                ret.add(Node(it, onlyStatic, null))
                break
            }
            if (it.startsWith(":")) {
                ret.add(Node(it, onlyStatic, null))
            } else {
                ret.add(Node(it, true, null))
            }
        }


        ret.last().value = value

        return ret
    }
}
