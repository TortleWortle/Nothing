package com.github.tortlewortle.something

import com.github.tortlewortle.nothing.CommandExecutor
import com.github.tortlewortle.nothing.NotFound

class MyCommandWithParam(override val ctx: MyContext) : CommandExecutor(ctx) {
    @NotFound("You must provide a message.")
    val message by required<String>()

    override fun exec() {
        ctx.replyEmbed(message)
    }
}