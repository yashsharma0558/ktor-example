package com.dev.plugins

import com.dev.routes.employeeRoute
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        employeeRoute()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
