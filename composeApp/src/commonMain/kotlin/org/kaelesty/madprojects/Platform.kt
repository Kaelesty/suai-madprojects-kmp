package org.kaelesty.madprojects

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform