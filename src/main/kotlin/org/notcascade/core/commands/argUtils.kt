package org.notcascade.core.commands

fun splitContent(msg: String): Array<String> {
    val ret = ArrayList<String>()
    var inString = false
    var buffer = ""

    msg.split(" ").fold(ret) { acc, s ->
        if (!inString) {
            if (s.startsWith("\"")) {
                buffer += s.substring(1)
                inString = true
            } else if (s != "") {
                acc.add(s)
            }
        } else {
            buffer += " "
            if (s.endsWith("\"") && !s.endsWith("\\\"")) {
                buffer += s.substring(0, s.length - 1)
                acc.add(buffer)
                buffer = ""
                inString = false
            } else {
                buffer += s
            }
        }
        acc
    }

    if (buffer != "") {
        return msg.split(" ").toTypedArray()
    }

    return ret.toTypedArray()
}

fun mapArgs(rawInput: String, rawRoute: String): Map<String, String> {
    val ret = HashMap<String, String>()
    val input = splitContent(rawInput)
    val routeParts = rawRoute.split(" ")

    val inputIt = input.iterator()
    val routeIt = routeParts.iterator()

    while (inputIt.hasNext() && routeIt.hasNext()) {
        val inp = inputIt.next()
        val route = routeIt.next()

        if (route.startsWith(":")) {
            val key = route.removeSuffix("?")
            ret.put(key.removePrefix(":"), inp)
        }
        if (route.startsWith("*")) {
            var buffer = inp
            inputIt.forEachRemaining {
                buffer += " "
                buffer += it
            }
            val key = route.removeSuffix("?")
            ret[key.removePrefix("*")] = buffer
        }
    }
    return ret
}

class InvalidRouteException(override val message: String) : RuntimeException()

// no real rules except that it can only have *concat at the end
fun validateRoute(route: String) {
    val concats = route.split(" ").count { it.startsWith("*") }

    if (concats > 1) {
        throw InvalidRouteException("cannot have multiple concat.")
    }
    if (concats == 1 && !route.split(" ").last().startsWith("*")) {
        throw InvalidRouteException("concat can only be at the end.")
    }
    val varNames = HashSet<String>()
    route.split(" ").forEach {
        if (it.startsWith(":") || it.startsWith("*")) {
            if (varNames.contains(it.substring(1))) {
                throw InvalidRouteException("no duplicate routes.")
            }
            varNames.add(it.substring(1))
        }
    }
}