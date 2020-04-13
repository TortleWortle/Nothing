package org.notcascade.modules.core

import org.notcascade.core.commands.CommandContext
import org.notcascade.core.commands.ICommandCore

class HelpCommand : ICommandCore {
    override fun onCommand(ctx: CommandContext) {
        ctx.reply(
            String.format("```\n%s```",
                ctx.cmdManager.commands.map {
                    it.key + " | " + it.description
                }.joinToString("\n")
            )
        )
    }

    override fun description(): String {
        return "Shows help"
    }

    override fun command(): String {
        return "help"
    }
}

