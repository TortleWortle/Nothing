package org.notcascade.modules.core

import org.koin.core.KoinComponent
import org.notcascade.core.commands.CommandContext
import org.notcascade.core.commands.ICommandCore
import kotlin.random.Random

class HelpCommand : ICommandCore, KoinComponent {

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


    fun testMiddleware(ctx : CommandContext) : Boolean {
        return true
    }

    override fun middleware(): ArrayList<(CommandContext) -> Boolean> {
        return arrayListOf(
            ::testMiddleware
        )
    }

    override fun permissions(): ArrayList<String> {
        return arrayListOf(
            "core.help",
            "dguild.MANAGE_SERVER"
        )
    }
}

