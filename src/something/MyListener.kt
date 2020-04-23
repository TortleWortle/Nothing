package com.github.tortlewortle.something

import com.github.tortlewortle.nothing.CommandManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.koin.core.KoinComponent
import org.koin.core.get

class MyListener() : ListenerAdapter(),
    KoinComponent {
    val commandManager: CommandManager<MessageReceivedEvent, MyContext> = get()

    override fun onMessageReceived(event: MessageReceivedEvent) {
        super.onMessageReceived(event)
        if (event.message.contentRaw.isEmpty()) return
        if (event.author.isBot) return

        GlobalScope.launch {
            commandManager.handle(event)
        }
    }
}