package org.notcascade

import com.uchuhimo.konf.Config
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.koin.core.KoinComponent
import org.koin.core.get


object MyJDA : KoinComponent {
    private val msgListener: MessageListener = get()
    private val config: Config = get()

    var jdabuilder: JDABuilder

    init {
        jdabuilder = JDABuilder.createDefault(config["bot.token"])
        jdabuilder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
        jdabuilder.setMemberCachePolicy(MemberCachePolicy.ALL)
        jdabuilder.addEventListeners(msgListener)
        jdabuilder.setActivity(Activity.listening("the voices"))
    }
    fun build() : JDA = jdabuilder.build()
}
