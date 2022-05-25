package id.walt.siwe.configuration

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureHTTP() {
    install(AutoHeadResponse)
    install(CORS) {
        allowHost("localhost:3000")
        allowCredentials = true
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
        //anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

}
