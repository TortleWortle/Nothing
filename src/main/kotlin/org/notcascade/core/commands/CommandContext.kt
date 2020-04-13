package org.notcascade.core.commands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.koin.core.KoinComponent
import org.koin.core.get
import org.slf4j.Logger
import java.lang.RuntimeException
import kotlin.random.Random
import kotlin.random.nextInt

class CommandContext(val event : GuildMessageReceivedEvent, val args : Map<String, String>) : KoinComponent {
    val logger : Logger = get()
    val cmdManager : CommandManager = get()

    fun reply(msg : String) {
        event.channel.sendMessage(msg).queue()
    }


    fun hasPermission(rawPerm : String) : Boolean {
        logger.debug(String.format("Checking perms %s for %s", rawPerm, event.author.asTag))

        var permission = rawPerm;
        var invert = false
        if (rawPerm.startsWith("!")) {
            permission = rawPerm.removePrefix("!")
            invert = true
        }

        val hasPerm = processPerm(event.member!!, permission)

        return if(invert) !hasPerm else hasPerm
    }

    private fun processPerm(member : Member, permission : String)  : Boolean {
        if (permission.startsWith("dguild.")) {
            val perm = Permission.valueOf(permission.removePrefix("dguild."))
            return member.hasPermission(perm)
        }

        return Random.nextInt(0..100) < 95
    }
}