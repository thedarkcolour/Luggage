package thedarkcolour.luggage.data

import net.minecraft.data.DataGenerator
import net.minecraftforge.common.data.LanguageProvider
import thedarkcolour.luggage.Luggage

class LangGenerator(implicit gen: DataGenerator) extends LanguageProvider(gen, Luggage.ID, "en_us") {
  override def addTranslations(): Unit = {
    addItem(Luggage.DUFFLE_BAG, "Duffle Bag")

    add("container.luggage.duffle_bag", "Duffle Bag")
  }
}
