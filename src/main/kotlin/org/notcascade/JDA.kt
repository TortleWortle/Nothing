package org.notcascade

import net.dv8tion.jda.api.JDABuilder
import org.koin.core.KoinComponent
import org.koin.core.get

class JDA() : KoinComponent {
    private val msgListener : MessageListener = get()
    private val config : Config = get()

    fun start() {
        val jda = JDABuilder.createDefault(config.token)
        println(msgListener)
        jda.addEventListeners(msgListener)

        jda.build()
    }
}