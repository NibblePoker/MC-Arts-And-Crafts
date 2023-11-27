package com.nibblepoker.artsandcrafts.interfaces;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.interfaces.components.CanvasGadget;
import com.nibblepoker.artsandcrafts.interfaces.components.TabGadget;
import com.nibblepoker.artsandcrafts.interfaces.components.TextButtonGadget;
import com.nibblepoker.artsandcrafts.logic.data.ArtData;
import com.nibblepoker.artsandcrafts.utils.ImageUtils;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

public class ImagePreviewScreen extends NPScreen {
    private final ArtData shownImageReference;

    // Draft-mode buttons
    private final TextButtonGadget deleteButton, modifyButton, saveButton;

    // Normal-mode buttons
    private final TextButtonGadget reportButton, remixButton, printButton;

    private final CanvasGadget previewCanvasGadget;

    private final boolean isDraft;

    protected ImagePreviewScreen(ArtData shownImageReference, boolean isDraft) {
        super(Component.translatable("text." + ArtsAndCraftsMod.MOD_ID +
                        ".designer_tab.title.preview." + (isDraft ? "draft" : "image")),
                284, 150);

        this.shownImageReference = shownImageReference;
        this.isDraft = isDraft;

        TabGadget headerTabGadget = new TabGadget(
                0, 0, this.getGuiWidth() - 76, 24,
                TabGadget.TabOrientation.NONE, (isDraft ? 34 : 9));
        TabGadget bodyTabGadget = new TabGadget(
                4, 24, this.getGuiWidth() - 8 - 76, this.getGuiHeight() - 24,
                TabGadget.TabOrientation.DOWN, (isDraft ? 35 : 10));

        TabGadget sideTab1Gadget = new TabGadget(
                204, 28, 80, 25,
                TabGadget.TabOrientation.RIGHT, (isDraft ? 4 : 7));
        TabGadget sideTab2Gadget = new TabGadget(
                204, 60, 80, 25,
                TabGadget.TabOrientation.RIGHT, 10);
        TabGadget sideTab3Gadget = new TabGadget(
                204, 92, 80, 25,
                TabGadget.TabOrientation.RIGHT, 21);

        this.deleteButton = new TextButtonGadget(206, 32, 73, 17,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.preview.delete"));
        this.reportButton = new TextButtonGadget(206, 32, 73, 17,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.preview.report"));

        this.modifyButton = new TextButtonGadget(206, 64, 73, 17,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.preview.modify"));
        this.remixButton = new TextButtonGadget(206, 64, 73, 17,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.preview.remix"));

        this.saveButton = new TextButtonGadget(206, 96, 73, 17,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.preview.save"));
        this.printButton = new TextButtonGadget(206, 96, 73, 17,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.preview.print"));

        this.previewCanvasGadget = new CanvasGadget(16, 32, 64, 64,
                ImageUtils.bytesToNativeImage(16, 16, this.shownImageReference.getImageData()));
        this.previewCanvasGadget.isModifiable = false;

        // Adding common gadgets
        this.addGadgets(headerTabGadget, bodyTabGadget, sideTab1Gadget, sideTab2Gadget, sideTab3Gadget,
                this.previewCanvasGadget);

        // Adding mode-dependant gadgets
        if(isDraft) {
            this.addGadgets(this.deleteButton, this.modifyButton, this.saveButton);
        } else {
            this.addGadgets(this.reportButton, this.remixButton, this.printButton);
        }
    }

    @Override
    public void renderRelative(GuiGraphics graphics, float partialTick, int originX, int originY, int relativeMouseX, int relativeMouseY) {
        // Rendering background, gadgets & sub-screens
        super.renderRelative(graphics, partialTick, originX, originY, relativeMouseX, relativeMouseY);

        // Rendering text
        ScreenUtils.drawShadedCenteredString(graphics, Minecraft.getInstance().font, this.title,
                originX + (208 / 2), originY + 12, ScreenUtils.COLOR_WHITE);
    }

    @Override
    public boolean mouseClickedRelative(int relativeClickX, int relativeClickY, int clickButton) {
        // Handling common actions
        // TODO

        // Handling mode-dependant actions
        if(this.isDraft) {

        } else {

        }

        return false;
    }

    private static void playClickSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    private static void playReportSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.0F));
    }

    private static void playDeleteSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.0F));
    }

    private static void playSavedSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.0F));
    }

    private static void playPrintSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.0F));
    }
}
