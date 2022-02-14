package thedarkcolour.luggage

import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.fml.InterModComms
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.{FMLClientSetupEvent, InterModEnqueueEvent}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.network.IContainerFactory
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries}
import top.theillusivec4.curios.api.{CuriosApi, SlotTypeMessage}

@Mod(Luggage.ID)
object Luggage {
  final val ID = "luggage"

  final val ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID)
  final val MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, ID)

  MENU_TYPES.register(FMLJavaModLoadingContext.get.getModEventBus)
  ITEMS.register(FMLJavaModLoadingContext.get.getModEventBus)

  final val DUFFLE_BAG = ITEMS.register("duffle_bag", () => new DuffleBagItem())
  final val DUFFLE_BAG_MENU = MENU_TYPES.register("duffle_bag", () => new MenuType(new IContainerFactory[DuffleBagMenu] {
    override def create(windowId: Int, inv: Inventory, data: FriendlyByteBuf): DuffleBagMenu = new DuffleBagMenu(windowId, inv, data)
  }))

  FMLJavaModLoadingContext.get.getModEventBus.addListener(clientSetup)
  FMLJavaModLoadingContext.get.getModEventBus.addListener(interModComms)

  def clientSetup(event: FMLClientSetupEvent): Unit = {
    event.enqueueWork(new Runnable {
      override def run(): Unit = {
        MenuScreens.register(DUFFLE_BAG_MENU.get(), (menu: DuffleBagMenu, playerInv, title) => {
          new DuffleBagScreen(menu, playerInv, title)
        })
      }
    })
  }

  def interModComms(event: InterModEnqueueEvent): Unit = {
    event.enqueueWork(new Runnable {
      override def run(): Unit = {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () => {
          new SlotTypeMessage.Builder("back").build()
        })
      }
    })
  }
}