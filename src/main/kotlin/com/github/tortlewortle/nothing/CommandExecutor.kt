package com.github.tortlewortle.nothing

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

annotation class NotFound(val message : String)

abstract class CommandExecutor(open val ctx: ICommandContext) : ICommandExecutor {
    lateinit var command : Command<ICommandContext>

    inline fun <reified T> optional(noinline fn : ((String) -> T?)? = null) = object : ParamDelegate<T>(T::class, fn) {}
    inline fun <reified T> required(noinline fn : ((String) -> T?)? = null) = object : ParamDelegateRequired<T>(T::class, fn) {}

    @Suppress("UNCHECKED_CAST")
    open class ParamDelegateRequired<T>(private val ckClass: KClass<*>, private val fn : ((String) -> T?)?) {
        operator fun getValue(thisRef: CommandExecutor, property: KProperty<*>): T {
            val ret = doParamMagic(ckClass, fn, thisRef, property)
            if (ret != null) {
                return ret
            }

            val annotation = property.annotations.find { it is NotFound } ?: throw RequiredParamNotFoundException("param not parsable", true)
            throw RequiredParamNotFoundException((annotation as NotFound).message)
        }
    }

    @Suppress("UNCHECKED_CAST")
    open class ParamDelegate<T>(private val ckClass: KClass<*>, private val fn : ((String) -> T?)?) {
        operator fun getValue(thisRef: CommandExecutor, property: KProperty<*>): T? =
            doParamMagic(ckClass, fn, thisRef, property)
    }
}
// 🚗
// Replace this with something inside of cmd manager :)
class ParamConverter<T : ICommandContext> {
    val typeStuff = HashMap<KClass<*>, (String, T) -> Any?>()

    init {
        typeStuff[String::class] = { param, _ ->
            param
        }
        typeStuff[Int::class] = { param, _ ->
            param.toInt()
        }
    }
}

fun <T> doParamMagic(ckClass: KClass<*>, fn : ((String) -> T?)?, thisRef: CommandExecutor, property: KProperty<*>) : T? {
    val param = thisRef.ctx.args[property.name] ?: return null
    if (fn != null) {
        return fn(param)
    }
    // probably make some reusable shit for this.
    return param as T

//    val converter = Convertibles.typeStuff.get(ckClass)
//
//    if (converter != null) {
//        return converter(param, thisRef.ctx) as T
//    }

    error(String.format("No converter found for %s.", ckClass.qualifiedName))
}