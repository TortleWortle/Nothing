package org.notcascade

import com.uchuhimo.konf.Config
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.koin.core.KoinComponent
import org.koin.core.get
import org.notcascade.core.commands.CommandManager

class MessageListener(private val commandManager: CommandManager) : ListenerAdapter(), KoinComponent {
    val config : Config = get();
    val prefix : String = config["bot.defaultPrefix"]

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        super.onGuildMessageReceived(event)
        if (!event.message.contentRaw.startsWith(prefix)) return
        val content = event.message.contentRaw.removePrefix(prefix)
        if (content.isEmpty()) return
        if (event.author.isBot) return

        GlobalScope.launch {
            commandManager.handleCommand(content, event)
        }
    }
}