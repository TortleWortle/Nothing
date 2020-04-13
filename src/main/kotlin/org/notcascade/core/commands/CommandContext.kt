package org.notcascade.core.commands

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.koin.core.KoinComponent
import org.koin.core.get

class CommandContext(val event : GuildMessageReceivedEvent, val args : Map<String, String>) : KoinComponent {
    val cmdManager : CommandManager = get()

    fun reply(msg : String) {
        event.channel.sendMessage(msg).queue()
    }
}