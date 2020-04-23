package com.github.tortlewortle.twitchbot

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.tortlewortle.nothing.*
import com.github.tortlewortle.nothing.builder.commandManager
import com.github.twitch4j.TwitchClient

import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent

fun main(args : Array<String>) {
    val client = TwitchClientBuilder.builder()
        .withEnableChat(true)
        .withChatAccount(OAuth2Credential("twitch", System.getenv("ACCESS_TOKEN")))
        .build()


    val cmdManager = cmdManager(client)

    client.chat.joinChannel("byepixel")
    client.eventManager.getEventHandler(SimpleEventHandler::class.java).onEvent(
        ChannelMessageEvent::class.java
    ) {
        cmdManager.handle(it)
    }

}

class MyContext(
    val event: ChannelMessageEvent,
    val client: TwitchClient,
    override val args: Map<String, String>
) : ICommandContext {
    fun reply(msg: String) {
       client.chat.sendMessage(event.channel.name, msg)
    }
}

class MyCommand(override val ctx : MyContext) : CommandExecutor(ctx) {
    override fun exec() {
        ctx.reply("boomer")
    }
}
class MyCommandWithParam(override val ctx : MyContext) : CommandExecutor(ctx) {
    val message by required<String>()
    override fun exec() {
        ctx.reply(message)
    }
}
fun cmdManager(client: TwitchClient): CommandManager<ChannelMessageEvent, MyContext> {
    return commandManager {
        prefix { ";" }
        middleware {
            true
        }
        contextBuilder = { event, args ->
            MyContext(event, client, args)
        }
        rawMessageBuilder = {
            it.message
        }
        defaultHandler = {
            println(String.format("Command: \"%s\" not found", it.message))
        }
        command {
            category = "core"
            description = "My test command."
            route = "ok"
            middleware { true }
            exec = { MyCommand(it) }
        }
        command {
            category = "core"
            description = "My test command."
            route = "say *message"
            middleware { true }
            exec = { MyCommandWithParam(it) }
        }
    }
}
