package com.github.tortlewortle.nothing.builder

import com.github.tortlewortle.nothing.*
import java.lang.RuntimeException


class CommandManagerBuilder<EVENT, CTX : ICommandContext>() {
    lateinit var contextBuilder : contextBuilder<EVENT, CTX>
    lateinit var rawMessageBuilder : rawMessageBuilder<EVENT>
    var defaultHandler : ((EVENT) -> Unit)? = null
    val commands = ArrayList<Command<CTX>>()
    val prefixes = ArrayList<(EVENT) -> (String)>()
    var errorHandler : ((RuntimeException, EVENT) -> Unit)? = null

    val middlewares = ArrayList<(CTX) -> Boolean>()

    fun build() : CommandManager<EVENT, CTX> {
        return CommandManager(
            contextBuilder,
            rawMessageBuilder,
            defaultHandler,
            errorHandler,
            commands,
            prefixes,
            middlewares
        )
    }

    fun prefix(fn : (EVENT) -> (String)) {
        prefixes.add(fn)
    }

    fun middleware(init : (CTX) -> Boolean) {
        middlewares.add(init)
    }

    fun command(init : CommandBuilder<CTX>.() -> Unit) {
        val cmd = CommandBuilder<CTX>().apply(init).build()

        commands.add(cmd)
    }
}

fun <EVENT, CTX : ICommandContext>commandManager(init : CommandManagerBuilder<EVENT, CTX>.() -> Unit) : CommandManager<EVENT, CTX> =
    CommandManagerBuilder<EVENT, CTX>().apply(init).build()