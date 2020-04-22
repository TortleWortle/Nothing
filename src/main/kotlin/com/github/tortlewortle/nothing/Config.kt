package com.github.tortlewortle.nothing

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import com.uchuhimo.konf.Feature
import com.uchuhimo.konf.source.toml
import com.uchuhimo.konf.source.yaml

object BotSpec : ConfigSpec("bot") {
    val defaultPrefix by required<String>()
    val token by required<String>()
}

data class Route(val key : String, val route : String, val alias : List<String>, val description : String?)
data class Alias(val route : String, val target : String, val alias : List<String>)

object CommandsSpec : ConfigSpec("commands") {
    val routes by required<List<Route>>()
    val alias by required<List<Alias>>()
}

fun getConfig(): Config {
    return Config {
        addSpec(BotSpec)
        addSpec(CommandsSpec)
    }
        .from.yaml.resource("config.yml")
        .from.yaml.resource("commands.yml")
        .from.yaml.file("config.yml", optional = true)
        .from.env()
        .validateRequired()
}