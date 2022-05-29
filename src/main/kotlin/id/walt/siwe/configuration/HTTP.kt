package id.walt.siwe.configuration

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureHTTP() {
    install(AutoHeadResponse)
    install(CORS) {
        allowCredentials = true
        maxAgeInSeconds = 1
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.Cookie)

        allowHost("localhost:3000", schemes = listOf("http", "https")) // For dev
        allowHost("siwe.walt-test.cloud", schemes = listOf("https")) // For test
        allowHost("siwe.walt.id", schemes = listOf("https")) // For demo
        //anyHost() // Don't do this in production if possible. Try to limit it.
    }
}
