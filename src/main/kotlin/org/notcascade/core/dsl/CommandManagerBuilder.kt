package org.notcascade.core.dsl

import org.notcascade.core.commands.CommandManager
import org.notcascade.core.commands.Module

class CommandManagerBuilder {
    private val commands = ArrayList<Command>()

    fun build(): CommandManager {
        return CommandManager(commands)
    }

    fun module(module: Module, init: ModuleBuilder.() -> Unit) {
        val builder = ModuleBuilder(module).apply(init)
        commands.addAll(builder.build())
    }

    fun module(init: () -> ArrayList<Command>) {
        commands.addAll(init())
    }
}

fun commandManager(init: CommandManagerBuilder.() -> Unit): CommandManager = CommandManagerBuilder().apply(init).build()