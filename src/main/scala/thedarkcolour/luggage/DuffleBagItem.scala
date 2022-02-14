package thedarkcolour.luggage

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.{CreativeModeTab, Item, ItemStack}
import net.minecraft.world.level.Level
import net.minecraft.world.{InteractionHand, InteractionResultHolder, SimpleMenuProvider}
import net.minecraftforge.network.NetworkHooks
import thedarkcolour.luggage.DuffleBagItem.CONTAINER_TITLE

class DuffleBagItem extends Item(new Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1)) {
  override def use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder[ItemStack] = {
    if (level.isClientSide) {
      InteractionResultHolder.success(player.getItemInHand(hand))
    } else {
      val duffle = player.getItemInHand(hand)
      val duffleSlot = if (hand == InteractionHand.MAIN_HAND) {
        player.getInventory.selected
      } else Inventory.SLOT_OFFHAND
      val title = if (duffle.hasCustomHoverName) {
        duffle.getHoverName
      } else CONTAINER_TITLE

      NetworkHooks.openGui(player.asInstanceOf[ServerPlayer], new SimpleMenuProvider((windowId, inventory, _) => {
        new DuffleBagMenu(windowId, inventory, duffleSlot)
      }, title), (buffer: FriendlyByteBuf) => {
        buffer.writeVarInt(duffleSlot)
      })

      InteractionResultHolder.consume(player.getItemInHand(hand))
    }
  }

  override def canFitInsideContainerItems: Boolean = false
}

object DuffleBagItem {
  final val CONTAINER_TITLE = new TranslatableComponent("container.luggage.duffle_bag")
}
