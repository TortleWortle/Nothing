package org.notcascade.core.commands

interface ICommandCore {
    fun onCommand(ctx : CommandContext)
    fun command() : String

    fun description() : String {
        return ""
    }

    fun middleware() : List<ICommandMiddleware> {
        return listOf();
    }

    fun permissions() : List<String> {
        return listOf()
    }
}