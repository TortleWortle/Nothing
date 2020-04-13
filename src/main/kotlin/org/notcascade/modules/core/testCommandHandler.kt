package org.notcascade.modules.core

import org.notcascade.core.commands.CommandContext

fun testCommandHandler(ctx : CommandContext) {
    ctx.reply("test func command handler")
}