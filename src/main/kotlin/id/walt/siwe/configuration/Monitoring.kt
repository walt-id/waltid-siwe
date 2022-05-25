package id.walt.siwe.configuration

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        /*format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val path = call.request.path()
            val headers = call.request.headers.entries().joinToString { "${it.key} -> [${it.value.joinToString()}]" }
            val cookies = call.request.cookies.rawCookies.entries.joinToString { "${it.key} -> ${it.value}" }
            val body = runBlocking { call.receiveOrNull<String>() }
            val query = call.request.queryString()
            val queryParamMap = call.request.queryParameters.entries().joinToString { "${it.key} -> [${it.value.joinToString()}]" }

            val resHeaders = call.response.headers.allValues().entries().joinToString { "${it.key} -> [${it.value.joinToString()}]" }
            """
            |REQUEST DEBUG LOG:
            |Request: $httpMethod [${path}]
            |    Headers: $headers
            |    Cookies: $cookies
            |    Body as String or null: $body
            |    QueryString: $query
            |    QueryParams: $queryParamMap
            |Response: [${status}]
            |    Headers: $resHeaders
            |----------------------------------------------------------------------------------""".trimMargin()
        }
        */
        // filter { call -> call.request.path().startsWith("/") }
        //callIdMdc("call-id")
    }
    /*install(CallId) {
        header(HttpHeaders.XRequestId)
        verify { callId: String ->
            callId.isNotEmpty()
        }
    }*/

}
