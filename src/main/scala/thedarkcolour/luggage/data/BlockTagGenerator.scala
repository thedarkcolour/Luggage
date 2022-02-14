package thedarkcolour.luggage.data

import net.minecraft.data.DataGenerator
import net.minecraft.data.tags.BlockTagsProvider
import net.minecraftforge.common.data.ExistingFileHelper
import thedarkcolour.luggage.Luggage

class BlockTagGenerator(implicit gen: DataGenerator, helper: ExistingFileHelper) extends BlockTagsProvider(gen, Luggage.ID, helper) {
  override def addTags(): Unit = { }
}
