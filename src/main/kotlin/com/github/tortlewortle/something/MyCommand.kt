package com.github.tortlewortle.something

import com.github.tortlewortle.nothing.CommandExecutor

class MyCommand(override val ctx: MyContext) : CommandExecutor(ctx) {
    override fun exec() {
        ctx.replyEmbed("Boomer")
    }
}