package com.nibblepoker.artsandcrafts.interfaces;

import com.mojang.blaze3d.platform.InputConstants;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.interfaces.components.*;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ImageEditorScreen extends Screen {
    private final static int SCREEN_WIDTH = 240;
    private final static int SCREEN_HEIGHT = 192;

    private final static Component textTools = Component.translatable(
            "text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.editor.tools");
    private final static Component textColors = Component.translatable(
            "text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.editor.colors");
    private final static Component textOptions = Component.translatable(
            "text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.editor.options");

    private final TabGadget headerTabGadget, bodyTabGadget;

    private final ArtButtonGadget goBackButton;

    private final CanvasGadget editorCanvas;

    private final ArtButtonGadget zoomOutButton, zoomInButton;
    private final ArtButtonGadget lessOpacityButton, moreOpacityButton;

    private final ArtButtonGadget pencilToolButton, eraserToolButton, pickerToolButton, bucketToolButton;

    private final TextButtonGadget frameMasksButton, colorBlindnessButton;

    private EEditorTool currentTool = EEditorTool.NONE;

    private final ColorSlotGadget mainColorSlot, secondaryColorSlot;
    private final ColorSlotGadget[] colorPaletteSlots = new ColorSlotGadget[10];

    protected ImageEditorScreen() {
        super(Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.title.editor"));

        this.headerTabGadget = new TabGadget(TabGadget.TabOrientation.NONE, 9);
        this.bodyTabGadget = new TabGadget(TabGadget.TabOrientation.DOWN, 10);

        this.goBackButton = new ArtButtonGadget(EArtButtonType.LEFT_MEDIUM, 5, 5);

        this.editorCanvas = new CanvasGadget(15, 31, 130, 130);
        this.editorCanvas.isModifiable = true;

        this.zoomOutButton = new ArtButtonGadget(EArtButtonType.EDITOR_MINUS, 15, 165);
        this.zoomInButton = new ArtButtonGadget(EArtButtonType.EDITOR_PLUS, 30, 165);
        this.lessOpacityButton = new ArtButtonGadget(EArtButtonType.EDITOR_OPACITY_MINUS, 45, 165);
        this.moreOpacityButton = new ArtButtonGadget(EArtButtonType.EDITOR_OPACITY_PLUS, 60, 165);

        this.pencilToolButton = new ArtButtonGadget(EArtButtonType.EDITOR_TOOL_PENCIL, 155, 40);
        this.eraserToolButton = new ArtButtonGadget(EArtButtonType.EDITOR_TOOL_ERASER, 173, 40);
        this.pickerToolButton = new ArtButtonGadget(EArtButtonType.EDITOR_TOOL_PICKER, 191, 40);
        this.bucketToolButton = new ArtButtonGadget(EArtButtonType.EDITOR_TOOL_BUCKET, 209, 40);

        this.frameMasksButton = new TextButtonGadget(152, 150, 75, 16,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.editor.options.frame_masks"));
        this.colorBlindnessButton = new TextButtonGadget(152, 168, 75, 16,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.editor.options.color_blindness"));

        this.mainColorSlot = new ColorSlotGadget(152, 71, EColorSlotGadgetType.LARGE,
                FastColor.ARGB32.color(0xFF, 0xFF, 0xFF, 0xFF));
        this.secondaryColorSlot = new ColorSlotGadget(160, 79, EColorSlotGadgetType.LARGE,
                FastColor.ARGB32.color(0xFF, 0x00, 0x00, 0x00));
        for(int i = 0; i < this.colorPaletteSlots.length; i++) {
            int startX = 151 + ((i % 5) * 16);
            int startY = 101 + ((i / 5) * 16);
            this.colorPaletteSlots[i] = new ColorSlotGadget(startX, startY, EColorSlotGadgetType.SMALL);
        }

        // Temporary changes for video demo
        this.zoomOutButton.isDisabled = true;
        this.zoomInButton.isDisabled = true;
        this.lessOpacityButton.isDisabled = true;
        this.moreOpacityButton.isDisabled = true;
        this.frameMasksButton.isDisabled = true;
        this.colorBlindnessButton.isDisabled = true;
        // Palette "SEAFOAM PALETTE" from Jimison3 on lospec.
        // See: https://lospec.com/palette-list/seafoam
        this.colorPaletteSlots[0].color = 0xFF37364e;
        this.colorPaletteSlots[1].color = 0xFF355d69;
        this.colorPaletteSlots[2].color = 0xFF6aae9d;
        this.colorPaletteSlots[3].color = 0xFFb9d4b4;
        this.colorPaletteSlots[4].color = 0xFFf4e9d4;
        this.colorPaletteSlots[5].color = 0xFFd0baa9;
        this.colorPaletteSlots[6].color = 0xFF9e8e91;
        this.colorPaletteSlots[7].color = 0xFF5b4a68;
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
        this.editorCanvas.render(graphics, partialTick, mouseX, mouseY, originX, originY);

        // Rendering text
        ScreenUtils.drawUnshadedCenteredString(graphics, Minecraft.getInstance().font, this.title,
                originX + (SCREEN_WIDTH / 2), originY + 12, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, textTools,
                originX + 150, originY + 29, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, textColors,
                originX + 150, originY + 60, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, textOptions,
                originX + 150, originY + 138, ScreenUtils.COLOR_WHITE);

        // Rendering the active colors & palette colors sections
        this.mainColorSlot.render(graphics, partialTick, mouseX, mouseY, originX, originY);
        this.secondaryColorSlot.render(graphics, partialTick, mouseX, mouseY, originX, originY);
        for(ColorSlotGadget colorPaletteSlot : this.colorPaletteSlots) {
            colorPaletteSlot.render(graphics, partialTick, mouseX, mouseY, originX, originY);
        }

        // Rendering the buttons
        this.goBackButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);

        this.zoomOutButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);
        this.zoomInButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);
        this.lessOpacityButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);
        this.moreOpacityButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);

        this.pencilToolButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);
        this.eraserToolButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);
        this.pickerToolButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);
        this.bucketToolButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);

        this.frameMasksButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);
        this.colorBlindnessButton.render(graphics, partialTick, mouseX, mouseY, originX, originY);
    }

    @Override
    public void tick() {
        // Updating the tool buttons' states
        this.pencilToolButton.isDisabled = this.currentTool == EEditorTool.PENCIL;
        this.eraserToolButton.isDisabled = this.currentTool == EEditorTool.ERASER;
        this.pickerToolButton.isDisabled = this.currentTool == EEditorTool.PICKER;
        this.bucketToolButton.isDisabled = this.currentTool == EEditorTool.BUCKET;

        super.tick();
    }

    @Override
    public boolean mouseClicked(double clickX, double clickY, int clickButton) {
        int originX = (this.width - SCREEN_WIDTH) / 2;
        int originY = (this.height - SCREEN_HEIGHT) / 2;

        if(this.goBackButton.onMouseClicked(clickX, clickY, originX, originY, clickButton, null)) {
            Minecraft.getInstance().setScreen(new DesignerTabScreen());
        } else if(this.pencilToolButton.onMouseClicked(clickX, clickY, originX, originY, clickButton, null)) {
            this.currentTool = EEditorTool.PENCIL;
        } else if(this.eraserToolButton.onMouseClicked(clickX, clickY, originX, originY, clickButton, null)) {
            this.currentTool = EEditorTool.ERASER;
        } else if(this.pickerToolButton.onMouseClicked(clickX, clickY, originX, originY, clickButton, null)) {
            this.currentTool = EEditorTool.PICKER;
        } else if(this.bucketToolButton.onMouseClicked(clickX, clickY, originX, originY, clickButton, null)) {
            this.currentTool = EEditorTool.BUCKET;
        }

        if(clickButton == 0 || clickButton == 1) {
            ColorSlotGadget clickedColorSlot = null;

            for(ColorSlotGadget colorSlotGadget : this.colorPaletteSlots) {
                if(colorSlotGadget.isMouseOver((int) clickX, (int) clickY, originX, originY)) {
                    clickedColorSlot = colorSlotGadget;
                    break;
                }
            }

            if(clickedColorSlot != null && this.currentTool != EEditorTool.BUCKET && this.currentTool != EEditorTool.NONE) {
                playColorSlotSound();
                switch(this.currentTool) {
                    case PENCIL:
                        clickedColorSlot.color = (clickButton == 0 ? this.mainColorSlot.color : this.secondaryColorSlot.color);
                        return true;
                    case ERASER:
                        clickedColorSlot.color = FastColor.ARGB32.color(0x00, 0x00, 0x00, 0x00);
                        return true;
                    case PICKER:
                        (clickButton == 0 ? this.mainColorSlot : this.secondaryColorSlot).color = clickedColorSlot.color;
                        return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int p_94711_, int p_94712_) {
        if(keyCode == InputConstants.KEY_P) {
            this.currentTool = EEditorTool.PENCIL;
            this.pencilToolButton.playClickSound();
        } else if(keyCode == InputConstants.KEY_E) {
            this.currentTool = EEditorTool.ERASER;
            this.eraserToolButton.playClickSound();
        } else if(keyCode == InputConstants.KEY_K) {
            this.currentTool = EEditorTool.PICKER;
            this.pickerToolButton.playClickSound();
        } else if(keyCode == InputConstants.KEY_B) {
            this.currentTool = EEditorTool.BUCKET;
            this.bucketToolButton.playClickSound();
        } else {
            return super.keyPressed(keyCode, p_94711_, p_94712_);
        }
        return true;
    }

    private static void playColorSlotSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    private enum EEditorTool {
        NONE,
        PENCIL,
        ERASER,
        PICKER,
        BUCKET;
    }
}
