package org.notcascade.core.commands

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.koin.core.KoinComponent
import org.koin.core.get
import org.notcascade.core.commands.ExecutableCommand
import org.notcascade.core.dsl.Command
import org.slf4j.Logger
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

open class CommandContext(val event: MessageReceivedEvent, val args: Map<String, String>) : KoinComponent {
    private val logger: Logger = get()
    val commandManager: CommandManager = get()

    val jda : JDA = event.jda
    val guild = event.guild
    val member = event.member
    val channel = event.channel
    val author = event.author

    fun reply(msg: String) {
        event.channel.sendMessage(msg).queue()
    }

    fun replyEmbed(msg : String) {
        reply(
            String.format(
                "```\n%s```",
                msg
            )
        )
    }

    fun hasPermission(rawPerm: String): Boolean {
        logger.debug(String.format("Checking perms %s for %s", rawPerm, event.author.asTag))

        var permission = rawPerm;
        var invert = false
        if (rawPerm.startsWith("!")) {
            permission = rawPerm.removePrefix("!")
            invert = true
        }

        val hasPerm = processPerm(event.member!!, permission)

        return if (invert) !hasPerm else hasPerm
    }

    private fun processPerm(member: Member, permission: String): Boolean {
        if (permission.startsWith("DISCORD.")) {
            val perm = Permission.valueOf(permission.removePrefix("DISCORD."))
            return member.hasPermission(perm)
        }

        return Random.nextInt(0..100) < 95
    }
}