package org.notcascade

import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.notcascade.core.commands.CommandManager
import org.notcascade.core.commands.Module
import org.notcascade.core.dsl.commandManager
import org.notcascade.modules.core.getCoreModuleCommands

fun main(args : Array<String>) {
    val myModule = module {
        single {
            getCommandManager()
        }
        single { MessageListener(get()) }
        single { Config(System.getenv("BOT_TOKEN")) }
    }

    startKoin {
        modules(myModule)
    }

    val jda = JDA()
    jda.start()
}

fun getCommandManager() : CommandManager {
    return commandManager {
        module { getCoreModuleCommands() }
        module(Module.MUSIC) {
            lambdaCommand("play *query") {
                description = "Plays Music"
                exec = { ctx ->
                    ctx.reply(String.format("Searching for `%s`", ctx.args["query"]))
                }
            }
        }
    }
}
