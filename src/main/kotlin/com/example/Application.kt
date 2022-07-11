package com.example

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.Serializable

fun main() {
    embeddedServer(Netty, port = 8080) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(CallLogging)
    install(ContentNegotiation){
        json()
    }
    routing {
        get("/username/{username}") {
            val username = call.parameters["username"]
            val header = call.request.headers["Connection"]


            if (username == "admin 2") {
                call.response.header(name = "CustomHeader", value = "admin 2")
                call.respond(message = "Hi ${username}, Greetings for the day. $header", status = HttpStatusCode.OK)
            } else {
                call.respondText { "Hi ${username}, Greetings for the day. You connection is: $header" }
            }

        }

        // add a new header or respond with a new header
        get("/newHeader") {
            call.response.header(name = "CustomHeader", value = "admin 2")
            call.respond(message = "", status = HttpStatusCode.OK)
        }

        // Query parameter
        get("/books") {
            val name = call.request.queryParameters["name"]
            val age = call.request.queryParameters["age"]?.toInt()
            val person = Person(name ?: "", age?.plus(4) ?: 0)

            call.respond(message = person, status = HttpStatusCode.OK)
        }

        // Serialization example
        get("/person") {
            val person = Person(name = "john", age = 25)
            try {
                call.respond(message = person, status = HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respond(message = person, status = HttpStatusCode.BadRequest)
            }
        }
    }
}

@Serializable
data class Person(
    val name: String,
    val age: Int
)