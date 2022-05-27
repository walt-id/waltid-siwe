package id.walt.siwe.configuration

import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*

fun Application.configureRouting() {
    install(DoubleReceive)
}
