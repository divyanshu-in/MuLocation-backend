package com.divyanshu_in.web_sockets

import com.divyanshu_in.models.MessageBody
import com.divyanshu_in.models.MessageType
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.*
import java.util.*


class Connection(val session: DefaultWebSocketSession) {
    var name: String? = null
}

fun Application.module() {
    val connections = Collections.synchronizedSet<Connection>(LinkedHashSet())

    install(WebSockets)
    routing {
        webSocket("/chat/{id}") {
            val thisConnection = Connection(this)

            MessageBody(
                null,
                null,
                null,
                MessageType.GENERAL_MSG,
                "You are connected, please enter a username to continue.").also {
                send(Json.encodeToString(it))
            }

            try {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue

                    var receivedText = frame.readText()
                    
                    if (thisConnection.name == null) {
                        thisConnection.name = Json.decodeFromString<MessageBody>(receivedText).username
                        connections += thisConnection
                        receivedText = Json.encodeToString(
                            MessageBody(
                                msg_type = MessageType.GENERAL_MSG,
                                message = "${thisConnection.name} joined the server!"))
                    }

                    connections.forEach {
                        if(thisConnection != it){
                            it.session.send(receivedText)
                        }
                    }
                }
            }catch (e: Exception){
                println(e.localizedMessage)
            }finally{
                connections.forEach {
                    if(thisConnection != it){
                        "${thisConnection.name} left the server!"
                        it.session.send(MessageBody(username=thisConnection.name, message = "user left the server!").parseToJsonString())
                    }
                }
                connections -= thisConnection
            }
        }
    }
}

fun MessageBody.parseToJsonString() = Json.encodeToString(this)

fun String.parseToMessageBody() = Json.decodeFromString<MessageBody>(this)