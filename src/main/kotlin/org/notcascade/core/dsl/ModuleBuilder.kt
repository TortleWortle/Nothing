package org.notcascade.core.dsl

import org.notcascade.core.commands.ExecutableCommand
import org.notcascade.core.commands.ICommandCore
import org.notcascade.core.commands.IExecutableCommand
import org.notcascade.core.commands.Module

fun moduleBuilder(module: Module, init: ModuleBuilder.() -> Unit): ArrayList<Command> =
    ModuleBuilder(module).apply(init).build()

class ModuleBuilder(private val module: Module) {
    var commands = ArrayList<Command>()

    fun build(): ArrayList<Command> {
        commands.forEach {
            it.module = module
        }
        return commands
    }

    fun command(key: String, init: CommandBuilder.() -> Unit) {
        val builder = CommandBuilder(key).apply(init)
        commands.add(builder.build())
    }
}