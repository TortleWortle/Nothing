package org.notcascade

import com.uchuhimo.konf.Config
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import org.koin.core.KoinComponent
import org.koin.core.get

class JDA() : KoinComponent {
    private val msgListener: MessageListener = get()
    private val config: Config = get()

    fun start() {
        val jda = JDABuilder.createDefault(config["bot.token"])
        println(msgListener)
        jda.addEventListeners(msgListener)

        jda.setActivity(Activity.listening("to the voices"))

//        jda.voice

        jda.build()
    }
}