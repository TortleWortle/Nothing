package org.notcascade.core.dsl

import org.notcascade.core.commands.CommandContext
import org.notcascade.core.commands.IExecutableCommand
import org.notcascade.core.commands.Module
import org.notcascade.core.middleware.PermissionMiddleware

abstract class MiddlewareAble (
    val middleware: ArrayList<(CommandContext) -> Boolean> = ArrayList(),
    val permissions: ArrayList<String> = ArrayList()) {

    init {
        val func = PermissionMiddleware(permissions)
        middleware.add(0, func)
    }

    fun addMiddleware( mw : (CommandContext) -> Boolean) {
        middleware.add(mw)
    }

    fun checkMiddleware(ctx : CommandContext) : Boolean {
        return middleware.all {
            it(ctx)
        }
    }
}

class Command(
    val key: String,
    var module: Module,
    var description: String,
    middleware: ArrayList<(CommandContext) -> Boolean> = ArrayList(),
    permissions: ArrayList<String> = ArrayList(),
    val new: (CommandContext) -> IExecutableCommand
) : MiddlewareAble(middleware, permissions) {
}

class CommandBuilder (private val key: String) {
    var module: Module = Module.ORPHAN
    var description: String = ""
    var factory: ((CommandContext) -> IExecutableCommand)? = null
    var middleware: ArrayList<(CommandContext) -> Boolean> = ArrayList()
    var permissions: ArrayList<String> = ArrayList()


    fun build(): Command {
        if (factory == null) throw Exception("Factory must exist")

        return Command(key, module, description, middleware, permissions, factory!!)
    }
}
