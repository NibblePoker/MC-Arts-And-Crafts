package com.nibblepoker.artsandcrafts.interfaces;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.interfaces.components.*;
import com.nibblepoker.artsandcrafts.logic.data.ArtData;
import com.nibblepoker.artsandcrafts.logic.data.StaticData;
import com.nibblepoker.artsandcrafts.logic.managers.ArtManager;
import com.nibblepoker.artsandcrafts.utils.ImageUtils;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

public class ImagePreviewScreen extends NPScreen {
    private final static Component TEXT_FORMAT = Component.translatable(
            "text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.preview.format");
    private final static Component TEXT_AUTHOR = Component.translatable(
            "text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.preview.author");

    private final ArtData shownImageReference;

    // Draft-mode buttons
    private final TextButtonGadget deleteButton, modifyButton, saveButton;

    // Normal-mode buttons
    private final TextButtonGadget reportButton, remixButton, printButton;

    // Common gadgets
    private final ArtButtonGadget goBackButton;
    private final CanvasGadget previewCanvasGadget;
    private final TextInputGadget formatTextBoxGadget, authorNameTextBoxGadget;

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

        this.goBackButton = new ArtButtonGadget(EArtButtonType.LEFT_MEDIUM, 5, 5);

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

        this.formatTextBoxGadget = new TextInputGadget(108, 15, 87, 41);
        this.formatTextBoxGadget.isEditable = false;
        this.formatTextBoxGadget.content = "TODO";

        this.authorNameTextBoxGadget = new TextInputGadget(108, 15, 87, 70);
        this.authorNameTextBoxGadget.isEditable = false;
        this.authorNameTextBoxGadget.content = "TODO";

        // Adding common gadgets
        this.addGadgets(headerTabGadget, bodyTabGadget, sideTab1Gadget, sideTab2Gadget, sideTab3Gadget,
                this.previewCanvasGadget, this.goBackButton, this.formatTextBoxGadget, this.formatTextBoxGadget,
                this.authorNameTextBoxGadget);

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

        graphics.drawString(Minecraft.getInstance().font, TEXT_FORMAT,
                originX + 85, originY + 31, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, TEXT_AUTHOR,
                originX + 85, originY + 60, ScreenUtils.COLOR_WHITE);
    }

    @Override
    public boolean mouseClickedRelative(int relativeClickX, int relativeClickY, int clickButton) {
        // Handling common actions
        if(this.goBackButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            Minecraft.getInstance().setScreen(new DesignerTabScreen());
        }

        // Handling mode-dependant actions

        // Draft-mode buttons
        //private final TextButtonGadget deleteButton, modifyButton, saveButton;
        // Normal-mode buttons
        //private final TextButtonGadget reportButton, remixButton, printButton;

        if(this.isDraft) {
            if(this.deleteButton.isMouseOver(relativeClickX, relativeClickY)) {
                StaticData.currentDraft = null;
                Minecraft.getInstance().setScreen(new DesignerTabScreen());
                playDeleteSound();
                return true;
            } else if(this.modifyButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
                Minecraft.getInstance().setScreen(new ImageEditorScreen(StaticData.currentDraft));
                return true;
            } else if(this.saveButton.isMouseOver(relativeClickX, relativeClickY)) {
                StaticData.currentDraft = null;
                ArtsAndCraftsMod.artManager.queueArtDataForSaving(this.shownImageReference);
                playSavedSound();
                Minecraft.getInstance().setScreen(new DesignerTabScreen());
                return true;
            }
        } else {
            if(this.reportButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
                // TODO: Report image
            } else if(this.remixButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
                // TODO: Make copy as draft & goto editor
            } else if(this.printButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
                // TODO: Ask server for blueprint item
                playPrintSound();
            }
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
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F));
    }

    private static void playPrintSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.0F));
    }
}
