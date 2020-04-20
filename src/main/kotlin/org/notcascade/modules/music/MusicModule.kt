package org.notcascade.modules.music

import org.notcascade.core.commands.CommandContext
import org.notcascade.core.commands.Module
import org.notcascade.core.dsl.Command
import org.notcascade.core.dsl.moduleBuilder

fun playCommand(ctx : CommandContext) {
    ctx.reply("Yesss")
}

//fun getMusicModule() : ArrayList<Command> {
//    return moduleBuilder(Module.MUSIC) {
//        command("music.play") {
//            handler { ::playCommand }
//            description = "Plays music!"
//            permissions = arrayListOf("music.play")
//        }
//    }
//}