package com.github.andrewgazelka.minebot.client


import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable


val client = HttpClient(CIO){
    install(JsonFeature){
        serializer = KotlinxSerializer()
    }
}

@Serializable
data class Block2D(val x: Int, val z: Int)

@Serializable
data class Selection2D(val from: Block2D, val to: Block2D)

@Serializable
data class Mine(val sel: Selection2D)

suspend fun sendMine(mine: Mine) {
    val response: HttpResponse = client.post("http://127.0.0.1:8080/mine"){
        contentType(ContentType.Application.Json)
        body = mine
    }

}



//fun main(){
//    runBlocking {
//        yes();
//    }
//}
