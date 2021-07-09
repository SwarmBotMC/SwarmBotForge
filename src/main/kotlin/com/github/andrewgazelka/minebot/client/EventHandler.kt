package com.github.andrewgazelka.minebot.client


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.item.ItemAxe
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

var pos1: BlockPos? = null
var pos2: BlockPos? = null

val player: EntityPlayerSP by lazy { Minecraft.getMinecraft().player }

val COMMAND_REGEX = """^#(\S+)\s?(.*)""".toRegex()

object EventHandler {
    @SubscribeEvent
    fun chatEvent(event: ClientChatEvent) {


        val match = COMMAND_REGEX.find(event.message) ?: return
        val name = match.groups[1]?.value ?: return;
        val args = match.groups[2]?.value?.split(" ") ?: emptyList();

        when (name) {
            "mine" -> {

                val start = pos1 ?: return;
                val end = pos2 ?: return;

                val from = Block2D(start.x, start.z)
                val to = Block2D(end.x, end.z)

                player.sendChatMessage("mining! $from $to")

                GlobalScope.launch {
                    sendMine(Mine(Selection2D(from, to)))
                }
            }
            else -> {
                player.sendMessage(TextComponentString("invalid command"))
            }
        }
    }

    @SubscribeEvent
    fun leftClick(event: PlayerInteractEvent.LeftClickBlock) {
        val tool = event.itemStack.item as? ItemAxe ?: return;
        if (pos1 != event.pos) {
            pos1 = event.pos
            player.sendMessage(TextComponentString("pos1 $pos1"))
        }
    }

    @SubscribeEvent
    fun rightClick(event: PlayerInteractEvent.RightClickBlock) {
        val tool = event.itemStack.item as? ItemAxe ?: return;
        if (pos2 != event.pos) {
            pos2 = event.pos
            player.sendMessage(TextComponentString("pos2 $pos2"))
        }
    }
}
