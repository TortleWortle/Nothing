package org.notcascade.modules.core

import org.koin.core.KoinComponent
import org.notcascade.core.commands.CommandContext
import org.notcascade.core.commands.ICommandCore
import org.notcascade.core.commands.Module

class HelpCommand : ICommandCore, KoinComponent {

    fun replyModule(ctx: CommandContext, module: String) {
        val mod = getModule(module)

        if (mod != null) {
            ctx.replyEmbed(
                ctx.commandManager.commands.filter {
                    it.module == mod
                }.joinToString("\n") {
                    it.key + " | " + it.description
                }
            )
        } else {
            ctx.replyEmbed("module does not exist")
        }
    }

    fun replyCommand(ctx : CommandContext, module : String, command : String) {
        val mod = getModule(module)

        if (mod != null) {
            ctx.replyEmbed(
                ctx.commandManager.commands.find {
                    it.module == mod
                }?.description ?: "Command not found"
            )
            return
        }
    }

    override fun onCommand(ctx: CommandContext) {
        val module = ctx.args["module"]
        val command = ctx.args["command"]

        if (module != null && command != null) return replyCommand(ctx, module, command)
        else if(module != null) return replyModule(ctx, module)

        ctx.replyEmbed(
            ctx.commandManager.commands.joinToString("\n") {
                it.key + " | " + it.description
            }
        )
    }

    private fun getModule(mod: String?): Module? {
        if (mod.isNullOrEmpty()) {
            return null
        }
        return try {
            Module.valueOf(mod.toUpperCase())
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun description(): String {
        return "Shows help"
    }

    override fun command(): String {
        return "core.help"
    }
}

