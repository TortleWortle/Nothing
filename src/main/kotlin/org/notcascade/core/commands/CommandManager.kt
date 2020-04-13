package org.notcascade.core.commands

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.koin.core.KoinComponent
import org.koin.core.get

import org.notcascade.core.dsl.Command
import org.slf4j.Logger

class CommandManager(val commands: ArrayList<Command>) : KoinComponent {
    val root = Node<Pair<Command, String>>()
    val logger: Logger = get()

    init {
        commands.forEach {
            // TODO: use i18n to fetch route
            root.addRoute(it.key, Pair(it, it.key))
        }
    }

    fun handleCommand(rawMsg: String, event: GuildMessageReceivedEvent) {
        if (event.author.isBot) return

        val pair = root.find(rawMsg)

        if (pair == null) {
            logger.info(String.format("%s called undefined command: %s", event.author.asTag, rawMsg))
            // resolve tags here
            return
        }

        val (cmd, route) = pair
        val ctx = CommandContext(event, mapArgs(rawMsg, route))

        val pass = cmd.middleware.all {
            it(ctx)
        }

        if (pass) {
            logger.info(String.format("%s executing command %s", event.author.asTag, cmd.key))
            cmd.exec(ctx)
        } else {
            logger.info(String.format("%s failed middleware checks for %s", event.author.asTag, cmd.key))
        }
    }
}