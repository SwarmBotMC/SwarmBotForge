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

suspend fun yes() {
    val response: HttpResponse = client.post("http://127.0.0.1:8080/mine"){
        contentType(ContentType.Application.Json)
        body = Mine(Selection2D(from=Block2D(0,0), to= Block2D(12,34)))
    }

}



//fun main(){
//    runBlocking {
//        yes();
//    }
//}
