package thedarkcolour.luggage.data

import net.minecraft.data.DataGenerator
import net.minecraft.data.tags.{BlockTagsProvider, ItemTagsProvider}
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraftforge.common.data.ExistingFileHelper
import thedarkcolour.luggage.Luggage
import top.theillusivec4.curios.api.CuriosApi

class ItemTagGenerator(blockTags: BlockTagsProvider)(implicit gen: DataGenerator, helper: ExistingFileHelper) extends ItemTagsProvider(gen, blockTags, Luggage.ID, helper) {
  override def addTags(): Unit = {
    val back = ItemTags.createOptional(new ResourceLocation(CuriosApi.MODID, "back"))

    tag(back).add(Luggage.DUFFLE_BAG.get())
  }

  override def getName: String = "Luggage Item Tags"
}
