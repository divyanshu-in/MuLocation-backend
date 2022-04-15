package com.divyanshu_in

import com.divyanshu_in.web_sockets.module
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8000, host = "192.168.29.128") {

        module()

    }.start(wait = true)
}
