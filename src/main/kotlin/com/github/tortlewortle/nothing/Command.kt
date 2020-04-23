package com.github.tortlewortle.nothing

data class Command<CTX : ICommandContext>(
    val route : String,
    val description : String = "",
    val category : String = "",
    val middleware : ArrayList<(CTX) -> Boolean>,
    val exec : (CTX) -> ICommandExecutor
) {}