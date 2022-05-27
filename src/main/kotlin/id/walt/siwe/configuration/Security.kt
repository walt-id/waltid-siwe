package id.walt.siwe.configuration

import id.walt.siwe.secretSessionSignKey
import io.ktor.server.application.*
import io.ktor.server.sessions.*

@kotlinx.serialization.Serializable
data class SiweSession(val nonce: String, val address: String? = null, val valid: Boolean = false)

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<SiweSession>("waltid-siwe-session") {
            transform(SessionTransportTransformerMessageAuthentication(secretSessionSignKey))
        }
    }
}
