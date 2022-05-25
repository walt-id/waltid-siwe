package id.walt.siwe.configuration

import io.ktor.server.routing.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {
    install(DoubleReceive)

}
