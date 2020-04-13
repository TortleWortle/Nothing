package org.notcascade.core.middleware

import org.notcascade.core.commands.CommandContext

fun PermissionMiddleware(permissions: ArrayList<String>) : (CommandContext) -> Boolean {
    return { ctx ->
        val allowed = permissions.all {
            ctx.hasPermission(it)
        }
        if (!allowed) ctx.reply("No permission pleb")
        allowed
    }
}