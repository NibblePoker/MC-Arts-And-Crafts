package com.nibblepoker.artsandcrafts.interfaces;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.interfaces.components.ArtButtonGadget;
import com.nibblepoker.artsandcrafts.interfaces.components.EArtButtonType;
import com.nibblepoker.artsandcrafts.interfaces.components.TabGadget;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ImageEditorScreen extends Screen {
    private static final ResourceLocation CANVAS_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/canvas_editor.png");

    private final static int SCREEN_WIDTH = 240;
    private final static int SCREEN_HEIGHT = 192;

    private final TabGadget headerTabGadget, bodyTabGadget;

    private final ArtButtonGadget goBackButton;

    private final ArtButtonGadget zoomOutButton, zoomInButton;
    private final ArtButtonGadget lessOpacityButton, moreOpacityButton;

    protected ImageEditorScreen() {
        super(Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.title.editor"));

        this.headerTabGadget = new TabGadget(TabGadget.TabOrientation.NONE, 9);
        this.bodyTabGadget = new TabGadget(TabGadget.TabOrientation.DOWN, 10);

        this.goBackButton = new ArtButtonGadget(EArtButtonType.LEFT_MEDIUM, 5, 5);

        this.zoomOutButton = new ArtButtonGadget(EArtButtonType.EDITOR_MINUS, 15, 165);
        this.zoomInButton = new ArtButtonGadget(EArtButtonType.EDITOR_PLUS, 30, 165);
        this.lessOpacityButton = new ArtButtonGadget(EArtButtonType.EDITOR_OPACITY_MINUS, 45, 165);
        this.moreOpacityButton = new ArtButtonGadget(EArtButtonType.EDITOR_OPACITY_PLUS, 60, 165);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        int originX = (this.width - SCREEN_WIDTH) / 2;
        int originY = (this.height - SCREEN_HEIGHT) / 2;

        // Rendering background tabs
        this.headerTabGadget.renderManualBackground(graphics,
                originX, originY, SCREEN_WIDTH, 24);
        this.bodyTabGadget.renderManualBackground(graphics, originX + 4, originY + 24,
                SCREEN_WIDTH - 8, SCREEN_HEIGHT - 24);

        // Rendering canvas
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        //RenderSystem.setShaderTexture(0, TEXTURE);
        graphics.blit(CANVAS_TEXTURE,
                originX + 15, originY + 31,
                130, 130,
                0, 0,
                130, 130,
                130, 130);


        // Rendering text
        ScreenUtils.drawUnshadedCenteredString(graphics, Minecraft.getInstance().font, this.title,
                originX + (SCREEN_WIDTH / 2), originY + 12, ScreenUtils.COLOR_TITLE);

        // Rendering the buttons
        this.goBackButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);
        this.zoomOutButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);
        this.zoomInButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);
        this.lessOpacityButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);
        this.moreOpacityButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);
    }

    @Override
    public boolean mouseClicked(double clickX, double clickY, int clickButton) {
        int originX = (this.width - SCREEN_WIDTH) / 2;
        int originY = (this.height - SCREEN_HEIGHT) / 2;

        if(this.goBackButton.onMouseClicked(clickX, clickY, originX, originY, clickButton, null)) {
            Minecraft.getInstance().setScreen(new DesignerTabScreen());
        }

        return false;
    }
}
