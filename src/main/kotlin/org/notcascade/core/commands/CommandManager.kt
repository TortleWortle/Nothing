package org.notcascade.core.commands

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.notcascade.core.dsl.Command

class CommandManager(val commands: ArrayList<Command>) {
    val root = Node<Pair<Command, String>>()

    init {
        commands.forEach {
            // TODO: use i18n to fetch route
            root.addRoute(it.key, Pair(it, it.key))
        }
    }

    fun handleCommand(rawMsg : String, event : GuildMessageReceivedEvent) {
        val pair = root.find(rawMsg)

        if (pair == null) {
            // resolve tags here
            return
        }

        val (cmd, route) = pair

        cmd.exec(
                CommandContext(event, mapArgs(rawMsg, route))
        )
    }
}