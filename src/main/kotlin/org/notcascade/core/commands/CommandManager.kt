package org.notcascade.core.commands

import com.uchuhimo.konf.Config
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
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
            if (route != null) {
                root.add(route.route, Pair(command, route.route))
                route.alias.forEach {
                    root.add(it, Pair(command, it))
                }
            }
        }

        alias.forEach { alias ->
            aliasRoot.add(alias.route, Pair(alias.route, alias.target))
            alias.alias.forEach {
                aliasRoot.add(it, Pair(it, alias.target))
            }
        }

    }


    fun getCmdAndArgs(rawMsg: String): Pair<Command, Map<String, String>>? {
        val pair = root.get(rawMsg)

        // if command has been found do your thing
        if (pair != null) {
            val (cmd, route) = pair
            val args = mapArgs(rawMsg, route)
            return Pair(cmd, args)
        }

        return null
    }

    fun getAliasCmdAndArgs(rawMsg: String): Pair<Command, Map<String, String>>? {
        // grab alias pair from rawMsg
        val alias = aliasRoot.get(rawMsg) ?: return null

        // grab route and targetRoute from alias
        val (route, targetRoute) = alias

        val aliasArgs = mapArgs(rawMsg, route)

        var target = targetRoute

        aliasArgs.forEach {
            val value = it.value
            value.replace("\"", "\\\"")
            target = target.replace("*" + it.key, "\"" + value + "\"")
                .replace(":" + it.key, "\"" + value + "\"")
        }

        // fetch original command from targetRoute
        val originalPair = root.get(target)

        // if original pair can't be found that means there's an alias with an invalid target.
        if (originalPair == null) {
            logger.error(String.format("Alias called with invalid target %s", target))
            return null
        }

        val (cmd, originalRoute) = originalPair
        val defaultArgs = mapArgs(target, originalRoute)

        return Pair(cmd, defaultArgs)
    }

    fun getTagCmdAndArgs(rawMsg: String): Pair<Command, Map<String, String>> {
        val args = HashMap<String, String>()

        class tagHandler(ctx: CommandContext) : ExecutableCommand(ctx) {
            override fun exec() {
                val tag = TagManager.getTag(ctx.event.guild.id, rawMsg)
                if (tag != null) {
                    ctx.event.channel.sendMessage(tag).queue()
                }
            }
        }

        val cmd = Command(
            module = Module.TAGS,
            description = String.format("tag handler"),
            key = "tag.handler",
            new = {
                tagHandler(it)
            }
        )


        return Pair(cmd, args)
    }

    fun handleCommand(rawMsg: String, event: MessageReceivedEvent) {
        val pair = getCmdAndArgs(rawMsg) ?: getAliasCmdAndArgs(rawMsg) ?: getTagCmdAndArgs(rawMsg)

        val (cmd, args) = pair
        val ctx = CommandContext(event, args)
        execCommand(cmd, ctx)
        return
    }

    private fun execCommand(cmd: Command, ctx: CommandContext) {
        if (!cmd.checkMiddleware(ctx)) {
            logger.info(String.format("%s failed middleware checks for %s", ctx.event.author.asTag, cmd.key))
        }

        logger.info(String.format("%s executing command %s", ctx.event.author.asTag, cmd.key))
        try {
            val realCmd = cmd.new(ctx)
            realCmd.exec()
        } catch (e: ParamFuckyWuckyException) {
            if (e.showUsage) {
                ctx.replyEmbed(cmd.description)
            } else {
                ctx.replyEmbed(e.message)
            }
        }
    }
}