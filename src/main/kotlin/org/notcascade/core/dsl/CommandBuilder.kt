package org.notcascade.core.dsl

import org.notcascade.core.commands.CommandContext
import org.notcascade.core.commands.Module
import org.notcascade.core.middleware.PermissionMiddleware
import kotlin.reflect.KFunction1

class Command(
    val key: String,
    var module: Module,
    var description: String,
    val middleware: ArrayList<(CommandContext) -> Boolean> = ArrayList(),
    val permissions: ArrayList<String> = ArrayList(),
    val exec: (CommandContext) -> Unit
) {
    init {
        val func = PermissionMiddleware(permissions)
        middleware.add(0, func)
    }
}

class CommandBuilder(private val key: String) {
    var module: Module = Module.ORPHAN
    var description: String = ""
    var exec: ((CommandContext) -> Unit)? = null
    var middleware: ArrayList<(CommandContext) -> Boolean> = ArrayList()
    var permissions: ArrayList<String> = ArrayList()

    fun handler(init: () -> KFunction1<CommandContext, Unit>) {
        val func = init()

        exec = {
            func.invoke(it)
        }
    }

    fun build(): Command {
        if (exec == null) throw Exception("Handler must exist")

        return Command(key, module, description, middleware, permissions, exec!!)
    }
}
