package org.notcascade.modules.permissions

import org.notcascade.core.commands.CommandContext
import org.notcascade.core.commands.Module
import org.notcascade.core.dsl.Command
import org.notcascade.core.dsl.moduleBuilder

fun addGroup(ctx : CommandContext) {
    val args = ctx.args
    ctx.replyEmbed(
        String.format(
            "Adding %s to %s", args["person"], args["group"]
        )
    )
}

fun getPermissionModule() : ArrayList<Command> {
    return moduleBuilder(Module.TAGS) {
        command("lp.user.add.group") {
            description = "Add a tag"
            handler {
                ::addGroup
            }
        }
    }
}