package com.github.tortlewortle.nothing.builder

import com.github.tortlewortle.nothing.Command
import com.github.tortlewortle.nothing.ICommandContext
import com.github.tortlewortle.nothing.ICommandExecutor

class CommandBuilder<CTX : ICommandContext> {
    lateinit var name : String
    lateinit var route : String
    var description = ""
    var category = "default"
    lateinit var exec : (CTX) -> ICommandExecutor
    val middlewares = ArrayList<(CTX) -> Boolean>()

    fun build() : Command<CTX> {
        return Command(name, route, description, category, middlewares, exec)
    }

    fun middleware(fn : (CTX) -> Boolean) {
        middlewares.add(fn)
    }
}