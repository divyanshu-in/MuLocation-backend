package com.divyanshu_in

import com.divyanshu_in.web_sockets.module
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8000) {
        routing {
            muLocationRouting()
        }
        module()

    }.start(wait = true)
}


fun Route.muLocationRouting() {
    route("") {

        get("{id?}") {
            call.respondText("Mulocation Backend Server Is Active!")
        }

    }
}