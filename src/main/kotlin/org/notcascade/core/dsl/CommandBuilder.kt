package org.notcascade.core.dsl

import org.notcascade.core.commands.CommandContext
import org.notcascade.core.commands.Module
import kotlin.reflect.KFunction1

class Command(val key : String, var module : Module, val description: String, val exec : (CommandContext) -> Unit)

class LambdaCommandBuilder(val key : String) {
    var module : Module = Module.ORPHAN
    var description: String = ""
    var exec : ((CommandContext) -> Unit)? = null

    fun build() : Command {
        return Command(key, module, description, exec!!)
    }
}

class CommandBuilder(private val key: String) {
    var module : Module = Module.ORPHAN
    var description: String = ""
    var exec: ((CommandContext) -> Unit)? = null

    fun handler(init: () -> KFunction1<CommandContext, Unit>) {
        val func = init()

        exec = {
            func.invoke(it)
        }
    }

    fun build() : Command {
        if (exec == null) throw Exception("Handler must exist")

        return Command(key, module, description, exec!!)
    }
}
