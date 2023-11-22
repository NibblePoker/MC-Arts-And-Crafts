package com.nibblepoker.artsandcrafts.interfaces;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.interfaces.components.ArtButtonGadget;
import com.nibblepoker.artsandcrafts.interfaces.components.EArtButtonType;
import com.nibblepoker.artsandcrafts.interfaces.components.TabGadget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DesignerTabScreen extends Screen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ArtsAndCraftsMod.MOD_ID,"textures/gui/gui_parts.png");

    private final TabGadget headerTabGadget, bodyTabGadget;

    private final ArtButtonGadget testButton;

    public DesignerTabScreen(Component title) {
        super(title);
        this.headerTabGadget = new TabGadget(TabGadget.TabOrientation.NONE);
        this.bodyTabGadget = new TabGadget(TabGadget.TabOrientation.DOWN, 15);

        // We don't want to render them through the normal pipeline.
        this.testButton = new ArtButtonGadget(EArtButtonType.RIGHT_LARGE, 0, 0);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Background is typically rendered first
        this.renderBackground(graphics);

        // Setting up the renderer and variables
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        // Magic sauce, I have no idea how I came up with that...
        int guiWidth = 176;
        int guiHeight = 144;
        int originX = (this.width - guiWidth) / 2;
        int originY = (this.height - guiHeight) / 2;

        // Rendering the GUI background
        this.headerTabGadget.renderManualBackground(graphics,
                originX, originY,
                guiWidth, 24);
        this.bodyTabGadget.renderManualBackground(graphics,
                originX + 4, originY + 24,
                guiWidth - 8, guiHeight - 24);

        // Rendering the buttons
        testButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);

        // Rendering any other standard widgets
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void tick() {
        this.testButton.tick();
        super.tick();
    }

    @Override
    public boolean mouseClicked(double clickX, double clickY, int clickButton) {
        // Magic sauce, I have no idea how I came up with that...
        int guiWidth = 176;
        int guiHeight = 144;
        int originX = (this.width - guiWidth) / 2;
        int originY = (this.height - guiHeight) / 2;

        System.out.println("Reee !");
        System.out.println(clickX);
        System.out.println(clickY);
        System.out.println(clickButton);

        this.testButton.onMouseClicked(clickX, clickY, originX, originY, clickButton, null);

        return false;
    }

    @Override
    public void onClose() {
        // Stopping any handlers.
        super.onClose();
    }

    @Override
    public void removed() {
        // Reverting to initial.
        super.removed();
    }
}
