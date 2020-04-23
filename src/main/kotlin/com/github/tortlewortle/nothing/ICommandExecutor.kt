package com.github.tortlewortle.nothing

interface ICommandExecutor {
    val ctx : ICommandContext
    fun exec()
}