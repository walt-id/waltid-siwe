package id.walt.siwe

import io.ktor.server.application.*
import id.walt.siwe.configuration.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.util.*

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function.
fun Application.module() {
    configuration()
    routes()
}

fun Application.routes() {
    routing {
        get("/nonce") {
            val newNonce = UUID.randomUUID().toString()
            call.sessions.set(WaltSiweSession(nonce = newNonce))
            call.respondText(newNonce)
        }
        post("/verify") {
            val request = call.receive<WaltSiweRequest>()
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
