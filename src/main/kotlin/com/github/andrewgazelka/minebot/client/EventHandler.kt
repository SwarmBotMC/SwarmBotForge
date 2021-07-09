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

var sel = false;

object EventHandler {
    @SubscribeEvent
    fun chatEvent(event: ClientChatEvent) {


        val match = COMMAND_REGEX.find(event.message) ?: return
        val name = match.groups[1]?.value ?: return;

        // cancel this because we are executing a command
        event.isCanceled = true;

        val args = match.groups[2]?.value?.split(" ") ?: emptyList();

        when (name) {
            "mine" -> {

                val start = pos1 ?: return;
                val end = pos2 ?: return;

                val from = Block2D(start.x, start.z)
                val to = Block2D(end.x, end.z)

                player.sendMessage(TextComponentString("mining! $from $to"))

                GlobalScope.launch {
                    sendMine(Mine(Selection2D(from, to)))
                }
            }
            "sel" -> {
                player.sendMessage(TextComponentString("toggled sel"))
                sel = !sel;
            }
            else -> {
                player.sendMessage(TextComponentString("invalid command"))
            }
        }
    }

    @SubscribeEvent
    fun leftClick(event: PlayerInteractEvent.LeftClickBlock) {
        if(!sel) return;
        event.isCanceled = true;
        val pos = event.pos;
        if (pos1 != pos) {
            player.sendMessage(TextComponentString("pos1 (${pos.x}, ${pos.z}"))
            pos1 = pos
        }
    }

    @SubscribeEvent
    fun rightClick(event: PlayerInteractEvent.RightClickBlock) {
        if(!sel) return;
        event.isCanceled = true;
        val pos = event.pos;
        if (pos2 != pos) {
            player.sendMessage(TextComponentString("pos2 (${pos.x}, ${pos.z}"))
            pos2 = pos
        }
    }
}
