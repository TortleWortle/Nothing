package org.notcascade.core.commands

import com.uchuhimo.konf.Config
import kotlinx.coroutines.flow.merge
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.koin.core.KoinComponent
import org.koin.core.get
import org.notcascade.Alias
import org.notcascade.Route

import org.notcascade.core.dsl.Command
import org.notcascade.modules.tags.TagManager
import org.slf4j.Logger


class CommandManager(val commands: ArrayList<Command>) : KoinComponent {
    val root = Node<Pair<Command, String>>()
    val aliasRoot = Node<Pair<String, String>>()
    val logger: Logger = get()
    val config: Config = get()
    val routes: List<Route> = config["commands.routes"]
    val alias: List<Alias> = config["commands.alias"]

    init {
        commands.forEach { command ->
            val route = routes.find { route ->
                command.key == route.key
            }
            // TODO: use i18n to fetch route
            if (route != null) {
                root.add(route.route, Pair(command, route.route))
                route.alias.forEach {
                    root.add(it, Pair(command, it))
                }
            }
        }

        alias.forEach {alias ->
            aliasRoot.add(alias.route, Pair(alias.route, alias.target))
            alias.alias.forEach {
                aliasRoot.add(it, Pair(it, alias.target))
            }
        }

    }


    fun handleCommand(rawMsg: String, event: GuildMessageReceivedEvent) {
        val pair = root.get(rawMsg)

        if (pair == null) {

            val alias = aliasRoot.get(rawMsg)

            if (alias == null) {
                logger.info(String.format("%s called undefined command: %s", event.author.asTag, rawMsg))
                handleTag(rawMsg, event)
                return
            }

            val (route, target) = alias

            val pair = root.get(target)

            if (pair == null) {
                logger.error(String.format("Alias called with invalid target %s", target))
                return
            }

            val (cmd, originalRoute) = pair

            val aliasArgs = mapArgs(rawMsg, route)

            val args = mapArgs(target, originalRoute)

            val ctx = CommandContext(event, cmd, mergeMaps(args, aliasArgs))

            execCommand(cmd, ctx)

            return
        }

        val (cmd, route) = pair

        val args = mapArgs(rawMsg, route)

        val ctx = CommandContext(event, cmd, args)

        execCommand(cmd, ctx)
    }

    fun mergeMaps(first : Map<String, String>, second : Map<String, String>) : Map<String, String> {
        val ret = HashMap<String, String>()
        first.forEach {
            ret.put(it.key, it.value)
        }
        second.forEach {
            ret.replace(it.key, it.value)
        }
        return ret
    }

    fun handleTag(rawMsg: String, event: GuildMessageReceivedEvent) {
        val tag = TagManager.getTag(event.guild.id, rawMsg)
        if (tag != null) {
            event.channel.sendMessage(tag).queue()
        }
    }

    private fun execCommand(cmd: Command, ctx: CommandContext) {
        val pass = cmd.middleware.all {
            it(ctx)
        }

        if (pass) {
            logger.info(String.format("%s executing command %s", ctx.event.author.asTag, cmd.key))
            cmd.exec(ctx)
        } else {
            logger.info(String.format("%s failed middleware checks for %s", ctx.event.author.asTag, cmd.key))
        }
    }
}