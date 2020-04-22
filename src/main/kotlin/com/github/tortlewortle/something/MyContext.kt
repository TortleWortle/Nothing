package com.github.tortlewortle.something

import com.github.tortlewortle.nothing.ICommandContext
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class MyContext(
    val event: MessageReceivedEvent,
    override val args: Map<String, String>
) : ICommandContext {
    fun replyEmbed(msg: String) {
        event.channel.sendMessage(String.format("```%s```", msg)).queue()
    }
}