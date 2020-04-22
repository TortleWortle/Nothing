package com.github.tortlewortle.nothing

import java.lang.RuntimeException

class RequiredParamNotFoundException(override val message: String, val showUsage : Boolean = false) : RuntimeException(message)