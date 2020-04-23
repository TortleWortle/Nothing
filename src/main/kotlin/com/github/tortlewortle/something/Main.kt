package com.github.tortlewortle.something

import com.github.tortlewortle.nothing.*
import com.github.tortlewortle.nothing.builder.commandManager
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    val commandManager = myBuilder()

    val config = getConfig()

    val myModule = module {
        single { commandManager }
        single { LoggerFactory.getLogger("default") }
        single { config }
    }


    startKoin {
        modules(myModule)
    }


    JDABuilder.createDefault(config["bot.token"])
        .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .addEventListeners(MyListener())
        .setActivity(Activity.listening("the voices"))
        .build()
}


fun myBuilder(): CommandManager<MessageReceivedEvent, MyContext> {
    return commandManager {
        prefix { ";" }
        prefix { "$" }
        middleware {
            true
        }
        contextBuilder = { event, args ->
            MyContext(event, args)
        }
        rawMessageBuilder = {
            it.message.contentRaw
        }
        defaultHandler = {
            it.channel.sendMessage("Command not found.").queue()
        }
        errorHandler = { e, event ->
            when (e) {
                is RequiredParamNotFoundException -> event.channel.sendMessage(e.message).queue()
            }
        }
        command {
            category = "core"
            description = "My test command."
            route = "test"
            middleware { true }
            exec = { MyCommand(it) }
        }
        command {
            category = "core"
            description = "Say something."
            route = "say *message?"
            exec = { MyCommandWithParam(it) }
        }
    }
}