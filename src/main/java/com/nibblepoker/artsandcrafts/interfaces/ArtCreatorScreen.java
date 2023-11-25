package com.nibblepoker.artsandcrafts.interfaces;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.interfaces.components.ArtButtonGadget;
import com.nibblepoker.artsandcrafts.interfaces.components.EArtButtonType;
import com.nibblepoker.artsandcrafts.interfaces.components.TabGadget;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ArtCreatorScreen extends NPScreen {
    private final TabGadget headerTabGadget, bodyTabGadget;

    private final ArtButtonGadget goBackButton;

    protected ArtCreatorScreen() {
        super(Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.title.create"),
                176, 144);

        this.headerTabGadget = new TabGadget(
                0, 0, this.getGuiWidth(), 24,
                TabGadget.TabOrientation.NONE);
        this.bodyTabGadget = new TabGadget(
                4, 24, this.getGuiWidth() - 8, this.getGuiHeight() - 24,
                TabGadget.TabOrientation.DOWN, 15);

        this.goBackButton = new ArtButtonGadget(EArtButtonType.LEFT_MEDIUM, 5, 5);

        this.addGadgets(this.headerTabGadget, this.bodyTabGadget, this.goBackButton);
    }

    @Override
    public void renderRelative(GuiGraphics graphics, float partialTick, int originX, int originY, int relativeMouseX, int relativeMouseY) {
        // Rendering background & gadgets
        super.renderRelative(graphics, partialTick, originX, originY, relativeMouseX, relativeMouseY);

        // Rendering text
        ScreenUtils.drawUnshadedCenteredString(graphics, Minecraft.getInstance().font, this.title,
                originX + (this.getGuiWidth() / 2), originY + 12, ScreenUtils.COLOR_TITLE);
    }

    @Override
    public boolean mouseClicked(double clickX, double clickY, int clickButton) {
        //Minecraft.getInstance().setScreen(new ImageEditorScreen());
        Minecraft.getInstance().setScreen(new ColorEditorSideScreen());
        return true;
    }
}
