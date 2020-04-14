package org.notcascade.modules.tags

import org.notcascade.core.commands.CommandContext
import org.notcascade.core.commands.Module
import org.notcascade.core.dsl.Command
import org.notcascade.core.dsl.moduleBuilder


typealias TagMap = HashMap<String, String>
typealias GuildID = String

object TagManager {
    private val tags = HashMap<GuildID, TagMap>()

    fun addTag(guild : GuildID, tag : String, content : String) {
        val gtags = tags.getOrPut(guild) { TagMap() }
        gtags.set(tag, content)
    }

    fun removeTag(guild : GuildID, tag : String) {
        val gtags = tags.getOrPut(guild) { TagMap() }
        gtags.remove(tag)
    }

    fun getTag(guild : GuildID, tag : String) : String? {
        val gtags = tags.getOrPut(guild) { TagMap() }

        return gtags[tag]
    }

    fun allTags(guild : GuildID) : TagMap {
        val gtags = tags.getOrPut(guild) { TagMap() }
        return gtags
    }
}

fun addTag(ctx : CommandContext) {
    TagManager.addTag(ctx.event.guild.id, ctx.args["name"] ?: error("Name arg missing"), ctx.args["content"] ?: error("Content arg missing"))
}

fun removeTag(ctx : CommandContext) {
    TagManager.removeTag(ctx.event.guild.id, ctx.args["name"] ?: error("Name arg missing"))
}

fun tagList(ctx : CommandContext) {
    val tags = TagManager.allTags(ctx.event.guild.id)
    val msg = tags.map {
        it.key + " | " + it.value
    }.joinToString("\n")

    ctx.replyEmbed(
        msg
    )
}

fun getTagModule() : ArrayList<Command> {
    return moduleBuilder(Module.TAGS) {
        command("tag.add") {
            description = "Add a tag"
            handler {
                ::addTag
            }
        }
        command("tag.remove") {
            description = "Remove a tag"
            handler {
                ::removeTag
            }
        }
        command("tag.list") {
            description = "List tags"
            handler {
                ::tagList
            }
        }
    }
}