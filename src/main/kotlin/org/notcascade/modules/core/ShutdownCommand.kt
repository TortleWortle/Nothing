package org.notcascade.modules.core

import org.koin.core.KoinComponent
import org.notcascade.core.commands.CommandContext
import org.notcascade.core.commands.ExecutableCommand
import kotlin.system.exitProcess

class ShutdownCommand(ctx: CommandContext) : ExecutableCommand(ctx),
    KoinComponent {
    override fun exec() {
            ctx.jda.shutdownNow()
        exitProcess(0)
    }
}