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
        route("/api") {
            get("/nonce") {
                val newNonce = UUID.randomUUID().toString()
                println("New nonce: $newNonce")
                call.sessions.set(SiweSession(nonce = newNonce))
                call.respondText(newNonce)
            }
            post("/verify") {
                val request = call.receiveOrNull<SiweRequest>()

                if (request == null) {
                    call.forbidden("Invalid or no request was sent."); return@post
                }

                val eip4361msg = Eip4361Message.fromString(request.message)
                println("EIP4361msg: $eip4361msg")
                println("EIP nonce: ${eip4361msg.nonce}")

                val session = call.sessions.get<SiweSession>()
                if (session == null) {
                    call.forbidden("Invalid or no session was set."); return@post
                }

                println("Session nonce: ${session.nonce}")
                if (session.nonce != eip4361msg.nonce) {
                    call.forbidden("Invalid nonce was set."); return@post
                }

                if (nonceBlacklists.contains(eip4361msg.nonce)) {
                    call.forbidden("Nonce reused."); return@post
                }

                val address = eip4361msg.address.lowercase()
                val signature = request.signature
                val origMsg = eip4361msg.toString()

                println("address: " + address)
                println("sig: " + signature)
                println("orig: " + origMsg)
                println("raw: " + call.receiveText())

                val signatureVerification = Web3jSignatureVerifier.verifySignature(address, signature, origMsg)
                if (!signatureVerification) {
                    call.forbidden("Invalid signature."); return@post
                }

                // TODO: Check expirationTime
                // TODO: Check notBefore

                nonceBlacklists.add(eip4361msg.nonce)

                call.sessions.set(SiweSession(eip4361msg.nonce, eip4361msg.address, true))
                call.respond(WaltSiweResponse(false, "Successfully signed in as ${eip4361msg.address}!"))
            }
            get("/personal_information") {
                val session = call.sessions.get<SiweSession>()
                if (session == null) {
                    call.forbidden("No or invalid session set."); return@get
                }
                if (!session.valid) {
                    call.forbidden("Session is not (yet) valid."); return@get
                }

                call.respond(SiweSession(session.nonce, session.address, session.valid))
            }
            post("/logout") {
                call.sessions.clear<SiweSession>()
                call.respond(WaltSiweResponse(false, "Logout."))
            }
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
