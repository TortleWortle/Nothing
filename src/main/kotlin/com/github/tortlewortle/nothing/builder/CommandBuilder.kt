package com.github.tortlewortle.nothing.builder

import com.github.tortlewortle.nothing.Command
import com.github.tortlewortle.nothing.ICommandContext
import com.github.tortlewortle.nothing.ICommandExecutor

class CommandBuilder<CTX : ICommandContext> {
    lateinit var route : String
    var description = ""
    var category = "default"
    lateinit var exec : (CTX) -> ICommandExecutor

    fun build() : Command<CTX> {
        return Command(route, description, category, exec)
    }
}