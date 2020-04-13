package org.notcascade

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import com.uchuhimo.konf.source.toml

object BotSpec : ConfigSpec("bot") {
    val token by required<String>()
}

fun getConfig(): Config {
    return Config {
        addSpec(BotSpec)
    }
        .from.toml.file("config.toml", optional = true)
        .from.env()
}