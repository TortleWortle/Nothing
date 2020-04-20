package org.notcascade.modules.moderation

import net.dv8tion.jda.api.entities.TextChannel
import org.notcascade.core.commands.CommandContext
import org.notcascade.core.commands.ExecutableCommand

class WipeChannelCommand(ctx: CommandContext) : ExecutableCommand(ctx) {
    override fun exec() {
        val messageList = ArrayList<String>()
        for (message in ctx.event.channel.iterableHistory) {
            messageList.add(message.id)
        }

        messageList.chunked(100).forEach {
            if (ctx.channel is TextChannel)
                ctx.channel.deleteMessagesByIds(it).queue()
        }
    }
}