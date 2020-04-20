package org.notcascade.modules.test

import net.dv8tion.jda.api.entities.Member
import org.notcascade.core.commands.CommandContext
import org.notcascade.core.commands.ExecutableCommand
import org.notcascade.core.commands.Module
import org.notcascade.core.commands.NotFound
import org.notcascade.core.dsl.Command
import org.notcascade.core.dsl.moduleBuilder
import org.notcascade.modules.moderation.WipeChannelCommand

class TestCommand(ctx: CommandContext) : ExecutableCommand(ctx) {
//    val person by required<String>()

    override fun exec() {
        ctx.replyEmbed("test")
    }
}

class KickCommand(ctx: CommandContext) : ExecutableCommand(ctx) {

    @NotFound("Cannot find member.")
    val person by required<Member>()

    val reason by optional<String>()

    override fun exec() {
        ctx.reply(String.format("Fuck off %s", person.asMention))
        ctx.replyEmbed(reason ?: "No reason given.")
        person.kick(reason).queue()
    }
}

class LPTestCommand(ctx: CommandContext) : ExecutableCommand(ctx) {
    val person by required<String>()
    val group by required<String>()

    override fun exec() {
        ctx.replyEmbed("boomer")
        ctx.replyEmbed(String.format("Moving %s to %s.", person, group))
    }
}

fun getTestModule() : ArrayList<Command> {

    return moduleBuilder(Module.ORPHAN) {
        command("test") {
            factory = { TestCommand(it) }
        }
        command("kick.user") {
            description = "Kick user."
            factory = { KickCommand(it) }
            permissions = arrayListOf(
                "DISCORD.KICK_MEMBERS"
            )
        }
        command("channel.clear") {
            factory = {
                WipeChannelCommand(it)
            }
        }
        command("lp.user.add.group") {
            factory = {
                LPTestCommand(it)
            }
        }
    }
}