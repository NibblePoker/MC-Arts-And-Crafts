package com.nibblepoker.artsandcrafts.interfaces;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.interfaces.components.*;
import com.nibblepoker.artsandcrafts.logic.data.ArtData;
import com.nibblepoker.artsandcrafts.logic.data.EArtFormat;
import com.nibblepoker.artsandcrafts.logic.data.StaticData;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ArtCreatorScreen extends NPScreen {
    private final static Component formatText = Component.translatable(
            "text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.creator.format");
    private final static Component resolutionText = Component.translatable(
            "text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.creator.resolution");

    private final TextButtonGadget regularFormatButton, carvingFormatButton;
    private final TextButtonGadget singleBlockSizeButton;
    private final TextButtonGadget continueButton;

    private final ArtButtonGadget goBackButton;

    protected ArtCreatorScreen() {
        super(Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.title.create"),
                160, 134);

        TabGadget headerTabGadget = new TabGadget(
                0, 0, this.getGuiWidth(), 24,
                TabGadget.TabOrientation.NONE, 34);
        TabGadget bodyTabGadget = new TabGadget(
                4, 24, this.getGuiWidth() - 8, this.getGuiHeight() - 48,
                TabGadget.TabOrientation.DOWN, 35);
        TabGadget confirmTabGadget = new TabGadget(
                80, this.getGuiHeight() - 24, 72, 24,
                TabGadget.TabOrientation.DOWN, 20);

        this.goBackButton = new ArtButtonGadget(EArtButtonType.LEFT_MEDIUM, 5, 5);

        this.regularFormatButton = new TextButtonGadget(9, 38, 142, 17,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.creator.regular_rgba"));
        this.carvingFormatButton = new TextButtonGadget(9, 57, 142, 17,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.creator.monochrome_carving"));

        this.singleBlockSizeButton = new TextButtonGadget(10, 87, 38, 17,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.creator.16x16"));

        this.continueButton = new TextButtonGadget(84, 112, 63, 17,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.creator.continue"));

        // Selecting default options
        this.regularFormatButton.isDisabled = true;
        this.singleBlockSizeButton.isDisabled = true;

        this.addGadgets(headerTabGadget, bodyTabGadget, confirmTabGadget, this.goBackButton,
                this.regularFormatButton, this.carvingFormatButton, this.singleBlockSizeButton, this.continueButton);
    }

    @Override
    public void renderRelative(GuiGraphics graphics, float partialTick, int originX, int originY, int relativeMouseX, int relativeMouseY) {
        // Rendering background & gadgets
        super.renderRelative(graphics, partialTick, originX, originY, relativeMouseX, relativeMouseY);

        // Rendering text
        ScreenUtils.drawShadedCenteredString(graphics, Minecraft.getInstance().font, this.title,
                originX + (this.getGuiWidth() / 2), originY + 12, ScreenUtils.COLOR_WHITE);

        graphics.drawString(Minecraft.getInstance().font, formatText,
                originX + 10, originY + 28, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, resolutionText,
                originX + 10, originY + 77, ScreenUtils.COLOR_WHITE);
    }

    @Override
    public boolean mouseClickedRelative(int relativeClickX, int relativeClickY, int clickButton) {
        if(this.regularFormatButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            this.selectFormatButton(this.regularFormatButton);
            return true;
        } else if(this.carvingFormatButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            this.selectFormatButton(this.carvingFormatButton);
            return true;
        } else if(this.singleBlockSizeButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            this.selectResolutionButton(this.singleBlockSizeButton);
            return true;
        } else if(this.continueButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            if(!this.createDraftImage()) {
                playErrorSound();
            }
            return true;
        } else if(this.goBackButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            Minecraft.getInstance().setScreen(new DesignerTabScreen());
            return true;
        }
        return false;
    }

    private boolean createDraftImage() {
        EArtFormat newDraftFormat = null;

        if(this.regularFormatButton.isDisabled && this.singleBlockSizeButton.isDisabled) {
            newDraftFormat = EArtFormat.ART_FULL_1X1_RGBA;
        }
        //else if(this.carvingFormatButton.isDisabled && this.singleBlockSizeButton.isDisabled) {}

        if(newDraftFormat != null) {
            StaticData.currentDraft = new ArtData(newDraftFormat);
            Minecraft.getInstance().setScreen(new ImageEditorScreen(StaticData.currentDraft));
            return true;
        } else {
            return false;
        }
    }

    private void selectFormatButton(TextButtonGadget selectedFormatButton) {
        this.regularFormatButton.isDisabled = false;
        this.carvingFormatButton.isDisabled = false;
        selectedFormatButton.isDisabled = true;
    }

    private void selectResolutionButton(TextButtonGadget selectedResolutionButton) {
        this.singleBlockSizeButton.isDisabled = false;
        selectedResolutionButton.isDisabled = true;
    }

    private static void playErrorSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.25F));
    }
}
