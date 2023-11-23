package com.nibblepoker.artsandcrafts.interfaces;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.interfaces.components.ArtButtonGadget;
import com.nibblepoker.artsandcrafts.interfaces.components.EArtButtonType;
import com.nibblepoker.artsandcrafts.interfaces.components.TabGadget;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ArtCreatorScreen extends Screen {
    private final static int SCREEN_WIDTH = 176;
    private final static int SCREEN_HEIGHT = 144;

    private final TabGadget headerTabGadget, bodyTabGadget;

    private final ArtButtonGadget goBackButton;

    protected ArtCreatorScreen() {
        super(Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.title.create"));

        this.headerTabGadget = new TabGadget(TabGadget.TabOrientation.NONE);
        this.bodyTabGadget = new TabGadget(TabGadget.TabOrientation.DOWN, 15);

        this.goBackButton = new ArtButtonGadget(EArtButtonType.LEFT_MEDIUM, 5, 5);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        int originX = (this.width - SCREEN_WIDTH) / 2;
        int originY = (this.height - SCREEN_HEIGHT) / 2;

        // Rendering background tabs
        this.headerTabGadget.renderManualBackground(graphics,
                originX, originY,
                SCREEN_WIDTH, 24);
        this.bodyTabGadget.renderManualBackground(graphics,
                originX + 4, originY + 24,
                SCREEN_WIDTH - 8, SCREEN_HEIGHT - 24);

        // Rendering text
        ScreenUtils.drawUnshadedCenteredString(graphics, Minecraft.getInstance().font, this.title,
                originX + (SCREEN_WIDTH / 2), originY + 12, ScreenUtils.COLOR_TITLE);

        // Rendering the buttons
        this.goBackButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);
    }

    @Override
    public boolean mouseClicked(double clickX, double clickY, int clickButton) {
        int originX = (this.width - SCREEN_WIDTH) / 2;
        int originY = (this.height - SCREEN_HEIGHT) / 2;

        if(this.goBackButton.onMouseClicked(clickX, clickY, originX, originY, clickButton, null)) {
            Minecraft.getInstance().setScreen(new DesignerTabScreen());
        } else {
            Minecraft.getInstance().setScreen(new ImageEditorScreen());
        }

        return false;
    }
}
