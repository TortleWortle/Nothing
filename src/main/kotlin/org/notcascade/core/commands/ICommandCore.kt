package org.notcascade.core.commands

interface ICommandCore {
    fun onCommand(ctx: CommandContext)
    fun command(): String

    fun description(): String {
        return ""
    }

    fun middleware(): ArrayList<(CommandContext) -> Boolean> {
        return ArrayList();
    }

    fun permissions(): ArrayList<String> {
        return ArrayList()
    }
}