package thedarkcolour.luggage

import net.minecraft.nbt.{CompoundTag, ListTag, Tag}
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.{AbstractContainerMenu, Slot}
import net.minecraft.world.item.ItemStack

class DuffleBagMenu(windowId: Int, inventory: Inventory, duffleSlot: Int)
  extends AbstractContainerMenu(Luggage.DUFFLE_BAG_MENU.get(), windowId) {

  def this(windowId: Int, inventory: Inventory, data: FriendlyByteBuf) = {
    this(windowId, inventory, data.readVarInt())
  }

  // From PlayerEnderChestContainer
  val container: SimpleContainer = new SimpleContainer(27) {
    override def fromTag(listTag: ListTag): Unit = {
      for (i <- 0 until getContainerSize) {
        setItem(i, ItemStack.EMPTY)
      }

      for (k <- 0 until listTag.size) {
        val tag = listTag.getCompound(k)
        val slot = tag.getByte("Slot") & 255

        if (slot >= 0 && slot < getContainerSize) {
          setItem(slot, ItemStack.of(tag))
        }
      }
    }

    override def createTag(): ListTag = {
      val list = new ListTag

      for (i <- 0 until getContainerSize) {
        val stack = getItem(i)

        if (!stack.isEmpty) {
          val tag = new CompoundTag()
          tag.putByte("Slot", i.toByte)
          stack.save(tag)
          list.add(tag)
        }
      }

      list
    }
  }

  {
    val duffle = inventory.getItem(duffleSlot)
    container.fromTag(duffle.getOrCreateTag().getList("Items", Tag.TAG_COMPOUND))

    // Inventory
    for (row <- 0 until 3) {
      for (col <- 0 until 9) {
        addSlot(new Slot(container, col + row * 9, 8 + col * 18, 18 + row * 18) {
          override def mayPlace(stack: ItemStack): Boolean = {
            stack.getItem.canFitInsideContainerItems
          }
        })
      }
    }

    // Player Inv
    for (row <- 0 until 3) {
      for (col <- 0 until 9) {
        val id = col + row * 9 + 9 // hotbar slots come earlier
        val x = 8 + col * 18
        val y = (103 - 18) + row * 18

        addSlot(new Slot(inventory, id, x, y))
      }
    }

    // Hotbar
    for (col <- 0 until 9) {
      val x = 8 + col * 18
      val y = 143

      if (col == duffleSlot) {
        // Prevent player from moving the backpack
        addSlot(new Slot(inventory, col, x, y) {
          override def mayPickup(p_40228_ : Player): Boolean = false
        })
      } else {
        addSlot(new Slot(inventory, col, x, y))
      }
    }
  }


  override def removed(player: Player): Unit = {
    super.removed(player)

    val duffle = inventory.getItem(duffleSlot)

    if (duffle.getItem == Luggage.DUFFLE_BAG.get()) {
      val duffleTag = duffle.getOrCreateTag()
      val list = container.createTag()
      duffleTag.put("Items", list)
    }
  }

  override def stillValid(player: Player): Boolean = {
    true // dunno when this would be false
  }

  override def quickMoveStack(player: Player, slotIndex: Int): ItemStack = {
    var stack = ItemStack.EMPTY
    val slot = slots.get(slotIndex)

    if (slot != null && slot.hasItem) {
      val stack1 = slot.getItem
      stack = stack1.copy()

      if (slotIndex < 27) {
        if (!moveItemStackTo(stack1, 27, slots.size(), true)) {
          return ItemStack.EMPTY
        }
      } else if (!moveItemStackTo(stack1, 0, 27, false)) {
        return ItemStack.EMPTY
      }

      if (stack1.isEmpty) {
        slot.set(ItemStack.EMPTY)
      } else {
        slot.setChanged()
      }
    }

    stack
  }
}