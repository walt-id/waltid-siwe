package id.walt.siwe.configuration

import id.walt.siwe.secretSessionSignKey
import io.ktor.server.application.*
import io.ktor.server.sessions.*

@kotlinx.serialization.Serializable
data class WaltSiweSession(val nonce: String, val address: String? = null, val valid: Boolean = false)

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<WaltSiweSession>("walt-siwe-session") {
            transform(SessionTransportTransformerMessageAuthentication(secretSessionSignKey))
        }
    }
}
