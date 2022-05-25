package id.walt.siwe.configuration

import io.ktor.server.sessions.*
import io.ktor.server.application.*

data class WaltSiweSession(val nonce: String)
fun Application.configureSecurity() {
    install(Sessions) {
        cookie<WaltSiweSession>("walt-siwe-session")
    }
}
