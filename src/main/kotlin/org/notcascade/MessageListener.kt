package org.notcascade

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.notcascade.core.commands.CommandManager

class MessageListener(private val commandManager: CommandManager) : ListenerAdapter() {
    private val prefix = ";"

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        super.onGuildMessageReceived(event)
        if (!event.message.contentRaw.startsWith(prefix)) return
        val content = event.message.contentRaw.substring(prefix.length)
        if (content.isEmpty()) return

        GlobalScope.launch {
            commandManager.handleCommand(content, event)
        }
    }
}