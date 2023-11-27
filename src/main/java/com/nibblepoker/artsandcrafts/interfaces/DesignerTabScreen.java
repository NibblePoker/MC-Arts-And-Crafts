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
public class DesignerTabScreen extends NPScreen {
    private final TabGadget headerTabGadget, bodyTabGadget;
    //private final ArtButtonGadget goBackButton;
    private final ArtButtonGadget testButton;

    public DesignerTabScreen() {
        super(Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.title.main"),
                176, 144);

        this.headerTabGadget = new TabGadget(
                0, 0, this.getGuiWidth(), 24,
                TabGadget.TabOrientation.NONE, 39);
        this.bodyTabGadget = new TabGadget(
                4, 24, this.getGuiWidth() - 8, this.getGuiHeight() - 24,
                TabGadget.TabOrientation.DOWN, 38);

        // We don't want to render them through the normal pipeline.
        //this.goBackButton = new ArtButtonGadget(EArtButtonType.LEFT_MEDIUM, 5, 5);
        //this.goBackButton.isDisabled = true;

        this.testButton = new ArtButtonGadget(EArtButtonType.RIGHT_LARGE, 50, 50);

        // Registering the gadgets.
        this.addGadgets(this.headerTabGadget, this.bodyTabGadget, this.testButton);
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
    public boolean mouseClickedRelative(int relativeClickX, int relativeClickY, int clickButton) {
        if(this.testButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            Minecraft.getInstance().setScreen(new ArtCreatorScreen());
            return true;
        }
        return false;
    }
}
