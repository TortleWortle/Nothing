package org.notcascade

import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.logger.SLF4JLogger
import org.notcascade.core.commands.CommandManager
import org.notcascade.core.commands.Module
import org.notcascade.core.dsl.commandManager
import org.notcascade.modules.core.getCoreModuleCommands
import org.notcascade.modules.music.getMusicModule
import org.notcascade.modules.permissions.getPermissionModule
import org.notcascade.modules.tags.getTagModule
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    val myModule = module {
        single {
            getCommandManager()
        }
        single { MessageListener(get()) }
        single { LoggerFactory.getLogger("default") }
        single { getConfig() }
    }


    startKoin {
        modules(myModule)
        logger(SLF4JLogger())
    }

    val jda = JDA()
    jda.start()
}

fun getCommandManager(): CommandManager {
    return commandManager {
        module { getCoreModuleCommands() }
        module { getMusicModule() }
        module { getTagModule() }
        module { getPermissionModule() }
    }
}
