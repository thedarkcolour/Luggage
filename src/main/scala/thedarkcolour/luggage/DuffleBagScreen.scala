package thedarkcolour.luggage

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.screens.inventory.{AbstractContainerScreen, MenuAccess}
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class DuffleBagScreen(menu: DuffleBagMenu, inventory: Inventory, title: Component) extends AbstractContainerScreen[DuffleBagMenu](menu, inventory, title) with MenuAccess[DuffleBagMenu] {
  passEvents = false
  imageHeight = 114 + 3 * 18
  inventoryLabelY = imageHeight - 94

  override def renderBg(stack: PoseStack, partialTicks: Float, mouseX: Int, mouseY: Int): Unit = {
    RenderSystem.setShader(() => GameRenderer.getPositionTexShader)
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    RenderSystem.setShaderTexture(0, DuffleBagScreen.CONTAINER_BACKGROUND)

    val i = (width - imageWidth) / 2
    val j = (height - imageHeight) / 2

    blit(stack, i, j, 0, 0, imageWidth, 3 * 18 + 17)
    blit(stack, i, j + 3 * 18 + 17, 0, 126, imageWidth, 96)
  }

  override def render(stack: PoseStack, mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
    renderBackground(stack)
    super.render(stack, mouseX, mouseY, partialTicks)
    renderTooltip(stack, mouseX, mouseY)
  }
}

object DuffleBagScreen {
  private final val CONTAINER_BACKGROUND = new ResourceLocation("textures/gui/container/generic_54.png")
}