package id.walt.siwe

import id.walt.siwe.configuration.*
import id.walt.siwe.eip4361.Eip4361Message
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import java.util.*


fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

lateinit var secretSessionSignKey: ByteArray

@Suppress("unused") // application.conf references the main function.
fun Application.module() {
    secretSessionSignKey = hex(environment.config.property("ktor.sessionkey").getString())

    configuration()
    routes()
}

@kotlinx.serialization.Serializable
data class WaltSiweResponse(val error: Boolean, val message: String)

val nonceBlacklists = HashSet<String>()

suspend fun ApplicationCall.forbidden(msg: String) {
    this.respond(HttpStatusCode.Forbidden, WaltSiweResponse(true, msg))
}

fun Application.routes() {
    routing {
        get("/nonce") {
            val newNonce = UUID.randomUUID().toString()
            call.sessions.set(WaltSiweSession(nonce = newNonce))
            call.respondText(newNonce)
        }
        post("/verify") {
            val request = call.receiveOrNull<WaltSiweRequest>()
            if (request == null) {
                call.forbidden("Invalid or no request was sent."); return@post
            }

            val eip4361msg = Eip4361Message.fromString(request.message)

            val session = call.sessions.get<WaltSiweSession>()
            if (session == null) {
                call.forbidden("Invalid or no session was set."); return@post
            }

            if (session.nonce != eip4361msg.nonce) {
                call.forbidden("Invalid nonce was set."); return@post
            }

            if (nonceBlacklists.contains(eip4361msg.nonce)) {
                call.forbidden("Nonce reused."); return@post
            }

            val address = eip4361msg.address.lowercase()
            val signature = request.signature
            val origMsg = eip4361msg.toString()

            val signatureVerification = Web3jSignatureVerifier.verifySignature(address, signature, origMsg)
            if (!signatureVerification) {
                call.forbidden("Invalid signature."); return@post
            }

            // TODO: Check expirationTime
            // TODO: Check notBefore


            nonceBlacklists.add(eip4361msg.nonce)

            call.sessions.set(WaltSiweSession(eip4361msg.nonce, eip4361msg.address, true))
            call.respond(WaltSiweResponse(false, "Successfully logged in as ${eip4361msg.address}!"))
        }
        get("/personal_information") {
            val session = call.sessions.get<WaltSiweSession>()
            if (session == null) {
                call.forbidden("No or invalid session set."); return@get
            }
            if (!session.valid) {
                call.forbidden("Session is not (yet) valid."); return@get
            }

            call.respond(WaltSiweSession(session.nonce, session.address, session.valid))
        }
    }
}

fun Application.configuration() {
    configureRouting()
    configureSecurity()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
}
