package thedarkcolour.luggage.data

import net.minecraft.data.DataGenerator
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent
import thedarkcolour.luggage.Luggage

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Luggage.ID)
object Data {
  @SubscribeEvent
  def gatherData(event: GatherDataEvent): Unit = {
    implicit val gen: DataGenerator = event.getGenerator
    implicit val helper: ExistingFileHelper = event.getExistingFileHelper

    if (event.includeClient()) {
      gen.addProvider(new LangGenerator())
    }
    if (event.includeServer()) {
      val blockTags = new BlockTagGenerator()
      //gen.addProvider(blockTags)
      gen.addProvider(new ItemTagGenerator(blockTags))
    }
  }
}
