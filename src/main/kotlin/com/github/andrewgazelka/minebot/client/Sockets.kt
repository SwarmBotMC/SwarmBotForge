package com.github.andrewgazelka.minebot.client


import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


//val client = HttpClient(CIO){
//    install(JsonFeature){
//        serializer = KotlinxSerializer()
//    }
//}

val client = HttpClient(CIO) {
    install(WebSockets)
}

val jsonEncoding = Json {
    classDiscriminator = "path"
}

@Serializable
data class Block2D(val x: Int, val z: Int)

@Serializable
data class Selection2D(val from: Block2D, val to: Block2D)

@Serializable
@SerialName("mine")
data class Mine(val sel: Selection2D) : Message()

@Serializable
sealed class Message

private val ws by lazy {

    val channel = Channel<String>()
    GlobalScope.launch {
        client.webSocketRaw(
            host = "127.0.0.1",
            port = 8080
        ) {

            channel.receiveAsFlow().collect { msg ->
                println("sent $msg")
                this@webSocketRaw.send(Frame.Text(msg))
            }
        }
    }
    channel
}

private suspend fun sendText(text: String) {
    ws.send(text)
}

suspend fun send(data: Message) {
    println("sending $data")
    val encoded = jsonEncoding.encodeToString(data)
    sendText(encoded)
}


//fun main() = runBlocking {
//    sendMine(Mine(Selection2D(Block2D(0, 1), Block2D(2, 4))))
//
//    delay(1000)
//}
