package org.notcascade.core.commands

import net.dv8tion.jda.api.entities.Member
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

interface IExecutableCommand {
    fun exec()
}

annotation class NotFound(val message : String)

class ParamFuckyWuckyException(override val message: String, val showUsage : Boolean = false) : RuntimeException(message)

abstract class ExecutableCommand(val ctx: CommandContext) : IExecutableCommand {
    var module : Module = Module.ORPHAN

    inline fun <reified T> optional(noinline fn : ((String) -> T?)? = null) = object : ParamDelegate<T>(T::class, fn) {}
    inline fun <reified T> required(noinline fn : ((String) -> T?)? = null) = object : ParamDelegateRequired<T>(T::class, fn) {}

    @Suppress("UNCHECKED_CAST")
    open class ParamDelegateRequired<T>(private val ckClass: KClass<*>, private val fn : ((String) -> T?)?) {
        operator fun getValue(thisRef: ExecutableCommand, property: KProperty<*>): T {
            val ret = doParamMagic(ckClass, fn, thisRef, property)
            if (ret != null) {
                return ret
            }

            val annotation = property.annotations.find { it is NotFound } ?: throw ParamFuckyWuckyException("param not parsable", true)
            throw ParamFuckyWuckyException((annotation as NotFound).message)
        }
    }

    @Suppress("UNCHECKED_CAST")
    open class ParamDelegate<T>(private val ckClass: KClass<*>, private val fn : ((String) -> T?)?) {
        operator fun getValue(thisRef: ExecutableCommand, property: KProperty<*>): T? =
            doParamMagic(ckClass, fn, thisRef, property)
    }
}
// 🚗
object Convertibles {
    val typeStuff = HashMap<KClass<*>, (String, CommandContext) -> Any?>()

    init {
        typeStuff[String::class] = { param, _ ->
            param
        }
        typeStuff[Int::class] = { param, _ ->
            param.toInt()
        }
        typeStuff[Member::class] = { param, ctx ->
            try {
                ctx.guild.getMemberById(param.replace(Regex("[^0-9]"), ""))
            } catch (e : Exception) {
                e.message?.let { ctx.replyEmbed(it) }
                null
            }
        }
    }
}

fun <T> doParamMagic(ckClass: KClass<*>, fn : ((String) -> T?)?, thisRef: ExecutableCommand, property: KProperty<*>) : T? {
    val param = thisRef.ctx.args[property.name] ?: return null
    if (fn != null) {
        return fn(param)
    }

    val converter = Convertibles.typeStuff.get(ckClass)

    if (converter != null) {
        return converter(param, thisRef.ctx) as T
    }

    error(String.format("No converter found for %s.", ckClass.qualifiedName))
}