package com.github.tortlewortle.nothing

typealias contextBuilder<EVENT, CTX> = (EVENT, args: Map<String, String>) -> CTX
typealias rawMessageBuilder<EVENT> = (EVENT) -> String

class CommandManager<EVENT, CTX : ICommandContext>(
    val contextBuilder: contextBuilder<EVENT, CTX>,
    val rawMessageBuilder: rawMessageBuilder<EVENT>,
    val defaultHandler: ((EVENT) -> Unit)?,
    val errorHandler: ((RuntimeException, EVENT) -> Unit)?,
    val commands: ArrayList<Command<CTX>>,
    val prefixes: ArrayList<(EVENT) -> (String)>,
    val middleware: ArrayList<(CTX) -> Boolean>
) {
    val root = Node<Command<CTX>>()

    init {
        commands.forEach {
            root.add(it.route, it)
        }
    }

    fun matchPrefix(event: EVENT, content: String): String? {
        for (fn in prefixes) {
            val prefix = fn(event)

            if (!content.startsWith(prefix)) {
                continue
            }
            val rawMsg = content.removePrefix(prefix)
            if (rawMsg.isBlank()) return null
            return rawMsg
        }
        return null
    }

    fun handle(event: EVENT) {
        val msgContent = rawMessageBuilder(event)
        val rawMsg = matchPrefix(event, msgContent) ?: return

        val cmd = root.get(rawMsg)

        if (cmd == null) {
            defaultHandler?.let { it(event) }
            return
        }

        val args = mapArgs(rawMsg, cmd.route)
        val ctx = contextBuilder(event, args)

        if (!middleware.all { it(ctx) }) return
        if (!cmd.middleware.all { it(ctx) }) return

        try {
            cmd.exec(ctx).exec()
        } catch (e: RuntimeException) {
            if (errorHandler != null) {
                errorHandler.invoke(e, event)
            } else {
                throw(e)
            }
        }
    }
}