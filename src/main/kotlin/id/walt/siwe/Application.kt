package id.walt.siwe

import io.ktor.server.application.*
import id.walt.siwe.configuration.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function.
fun Application.module() {
    configuration()
    routes()
}

fun Application.routes() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/double-receive") {
            val first = call.receiveText()
            val theSame = call.receiveText()
            call.respondText(first + " " + theSame)
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
