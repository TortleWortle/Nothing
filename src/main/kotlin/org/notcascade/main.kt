package org.notcascade

import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.notcascade.core.commands.CommandManager
import org.notcascade.core.dsl.commandManager
import org.notcascade.modules.tags.getTagModule
import org.notcascade.modules.test.getTestModule
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
    }

    MyJDA.build()
}

fun getCommandManager(): CommandManager {
    return commandManager {
//        module { getCoreModuleCommands() }
//        module { getMusicModule() }
        module { getTagModule() }
//        module { getPermissionModule() }
        module { getTestModule() }
    }
}
