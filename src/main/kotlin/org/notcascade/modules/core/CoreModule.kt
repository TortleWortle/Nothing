package org.notcascade.modules.core

import org.notcascade.core.commands.Module
import org.notcascade.core.dsl.Command
import org.notcascade.core.dsl.moduleBuilder

fun getCoreModuleCommands(): ArrayList<Command> {
    return moduleBuilder(Module.CORE) {
        classCommand { HelpCommand() }
    }
}