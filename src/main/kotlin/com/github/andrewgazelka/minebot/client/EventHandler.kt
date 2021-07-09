package com.github.andrewgazelka.minebot.client


import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object EventHandler {
    @SubscribeEvent
    fun pickupItem(event: ClientChatEvent) {
        println(event.message)
    }
}
