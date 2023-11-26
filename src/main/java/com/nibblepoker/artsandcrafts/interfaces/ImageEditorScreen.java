package com.nibblepoker.artsandcrafts.interfaces;

import com.mojang.blaze3d.platform.InputConstants;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.interfaces.components.*;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;

public class ImageEditorScreen extends NPScreen {
    private static final ResourceLocation COLOR_POINTERS_TEXTURE = new ResourceLocation(
            ArtsAndCraftsMod.MOD_ID,"textures/gui/color_pointer.png");

    private final static Component textTools = Component.translatable(
            "text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.editor.tools");
    private final static Component textColors = Component.translatable(
            "text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.editor.colors");
    private final static Component textOptions = Component.translatable(
            "text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.editor.options");

    private final ColorEditorSideScreen colorEditorScreen;

    private final TabGadget headerTabGadget, bodyTabGadget;

    private final ArtButtonGadget goBackButton;

    private final CanvasGadget editorCanvas;

    //private final ArtButtonGadget zoomOutButton, zoomInButton;
    private final ArtButtonGadget lessOpacityButton, moreOpacityButton;

    private final ArtButtonGadget pencilToolButton, eraserToolButton, pickerToolButton, bucketToolButton, colorEditorToolButton;

    private final TextButtonGadget frameMasksButton, colorBlindnessButton;

    private EEditorTool currentTool = EEditorTool.NONE;

    private final ColorSlotGadget mainColorSlotGadget, secondaryColorSlotGadget;
    private final ColorSlotGadget[] colorPaletteSlotGadgets = new ColorSlotGadget[10];

    // Other variables
    private ColorSlotGadget editedColorSlot = null;

    protected ImageEditorScreen() {
        super(Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.title.editor"),
                240, 192);

        this.colorEditorScreen = new ColorEditorSideScreen();
        this.colorEditorScreen.setGuiOffsetX(this.getGuiWidth() - 4);
        this.colorEditorScreen.setGuiOffsetY(32);
        this.colorEditorScreen.setEnabled(false);
        this.addSubScreens(this.colorEditorScreen);

        this.headerTabGadget = new TabGadget(
                0, 0, this.getGuiWidth(), 24,
                TabGadget.TabOrientation.NONE, 9);
        this.bodyTabGadget = new TabGadget(
                4, 24, this.getGuiWidth() - 8, this.getGuiHeight() - 24,
                TabGadget.TabOrientation.DOWN, 10);

        this.goBackButton = new ArtButtonGadget(EArtButtonType.LEFT_MEDIUM, 5, 5);

        this.editorCanvas = new CanvasGadget(15, 31, 130, 130);
        this.editorCanvas.isModifiable = true;

        //this.zoomOutButton = new ArtButtonGadget(EArtButtonType.EDITOR_MINUS, 15, 165);
        //this.zoomInButton = new ArtButtonGadget(EArtButtonType.EDITOR_PLUS, 30, 165);
        this.lessOpacityButton = new ArtButtonGadget(EArtButtonType.EDITOR_OPACITY_MINUS, 91, 165);
        this.moreOpacityButton = new ArtButtonGadget(EArtButtonType.EDITOR_OPACITY_PLUS, 133, 165);

        this.pencilToolButton = new ArtButtonGadget(EArtButtonType.EDITOR_TOOL_PENCIL, 151, 40);
        this.eraserToolButton = new ArtButtonGadget(EArtButtonType.EDITOR_TOOL_ERASER, 167, 40);
        this.pickerToolButton = new ArtButtonGadget(EArtButtonType.EDITOR_TOOL_PICKER, 183, 40);
        this.bucketToolButton = new ArtButtonGadget(EArtButtonType.EDITOR_TOOL_BUCKET, 199, 40);
        this.colorEditorToolButton = new ArtButtonGadget(EArtButtonType.EDITOR_TOOL_COLOR_FULL, 215, 40);

        this.frameMasksButton = new TextButtonGadget(152, 150, 75, 16,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.editor.options.frame_masks"));
        this.colorBlindnessButton = new TextButtonGadget(152, 168, 75, 16,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.editor.options.color_blindness"));

        this.mainColorSlotGadget = new ColorSlotGadget(152, 71, EColorSlotGadgetType.LARGE,
                FastColor.ARGB32.color(0xFF, 0xFF, 0xFF, 0xFF));
        this.secondaryColorSlotGadget = new ColorSlotGadget(160, 79, EColorSlotGadgetType.LARGE,
                FastColor.ARGB32.color(0xFF, 0x00, 0x00, 0x00));
        for(int i = 0; i < this.colorPaletteSlotGadgets.length; i++) {
            int startX = 151 + ((i % 5) * 16);
            int startY = 101 + ((i / 5) * 16);
            this.colorPaletteSlotGadgets[i] = new ColorSlotGadget(startX, startY, EColorSlotGadgetType.SMALL);
        }

        // Temporary changes for video demo
        //this.zoomOutButton.isDisabled = true;
        //this.zoomInButton.isDisabled = true;
        this.frameMasksButton.isDisabled = true;
        this.colorBlindnessButton.isDisabled = true;
        // Palette "SEAFOAM PALETTE" from Jimison3 on lospec.
        // See: https://lospec.com/palette-list/seafoam
        this.colorPaletteSlotGadgets[0].color = FastColor.ARGB32.color(0xFF, 0x37, 0x36, 0x4e);
        this.colorPaletteSlotGadgets[1].color = FastColor.ARGB32.color(0xFF, 0x35, 0x5d, 0x69);
        this.colorPaletteSlotGadgets[2].color = FastColor.ARGB32.color(0xFF, 0x6a, 0xae, 0x9d);
        this.colorPaletteSlotGadgets[3].color = FastColor.ARGB32.color(0xFF, 0xb9, 0xd4, 0xb4);
        this.colorPaletteSlotGadgets[4].color = FastColor.ARGB32.color(0xFF, 0xf4, 0xe9, 0xd4);
        this.colorPaletteSlotGadgets[5].color = FastColor.ARGB32.color(0xFF, 0xd0, 0xba, 0xa9);
        this.colorPaletteSlotGadgets[6].color = FastColor.ARGB32.color(0xFF, 0x9e, 0x8e, 0x91);
        this.colorPaletteSlotGadgets[7].color = FastColor.ARGB32.color(0xFF, 0x5b, 0x4a, 0x68);

        this.addGadgets(
                this.headerTabGadget, this.bodyTabGadget, this.goBackButton, this.editorCanvas, this.lessOpacityButton,
                this.moreOpacityButton, this.pencilToolButton, this.eraserToolButton, this.pickerToolButton,
                this.bucketToolButton, this.colorEditorToolButton, this.frameMasksButton, this.colorBlindnessButton,
                this.secondaryColorSlotGadget, this.mainColorSlotGadget);
        this.addGadgets(this.colorPaletteSlotGadgets);
    }

    @Override
    public void renderRelative(GuiGraphics graphics, float partialTick, int originX, int originY, int relativeMouseX, int relativeMouseY) {
        // Rendering background, gadgets & sub-screens
        super.renderRelative(graphics, partialTick, originX, originY, relativeMouseX, relativeMouseY);

        // Rendering text
        ScreenUtils.drawShadedCenteredString(graphics, Minecraft.getInstance().font, this.title,
                originX + (this.getGuiWidth() / 2), originY + 12, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, textTools,
                originX + 150, originY + 29, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, textColors,
                originX + 150, originY + 60, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, textOptions,
                originX + 150, originY + 138, ScreenUtils.COLOR_WHITE);

        ScreenUtils.drawShadedCenteredString(graphics, Minecraft.getInstance().font,
                (int)(this.editorCanvas.getOpacity() * 100) + "%",
                originX + 118, originY + 172, ScreenUtils.COLOR_WHITE);
    }

    @Override
    public void tick() {
        // Updating the tool buttons' states
        this.pencilToolButton.isDisabled = this.currentTool == EEditorTool.PENCIL;
        this.eraserToolButton.isDisabled = this.currentTool == EEditorTool.ERASER;
        this.pickerToolButton.isDisabled = this.currentTool == EEditorTool.PICKER;
        this.bucketToolButton.isDisabled = this.currentTool == EEditorTool.BUCKET;
        this.colorEditorToolButton.isDisabled = this.currentTool == EEditorTool.COLOR_EDITOR;

        // Updating the canvas's internal image if needed & other related stuff
        this.editorCanvas.tick();
        this.lessOpacityButton.isDisabled = this.editorCanvas.getOpacity() <= 0.5F;
        this.moreOpacityButton.isDisabled = this.editorCanvas.getOpacity() == 1.0F;

        // Updating the color editor
        this.colorEditorScreen.setEnabled(this.currentTool == EEditorTool.COLOR_EDITOR);
        if(this.currentTool != EEditorTool.COLOR_EDITOR && this.editedColorSlot != null) {
            this.editedColorSlot.drawTarget = false;
            this.editedColorSlot = null;
        }
        if(this.currentTool == EEditorTool.COLOR_EDITOR && this.editedColorSlot != null) {
            this.editedColorSlot.color = FastColor.ARGB32.color(
                    this.colorEditorScreen.getA(), this.colorEditorScreen.getR(),
                    this.colorEditorScreen.getG(), this.colorEditorScreen.getB()
            );
        }

        // Updating the root screen's offset
        if(this.colorEditorScreen.isEnabled()) {
            this.setGuiOffsetX(((this.colorEditorScreen.getGuiWidth() / 2) - 4) * -1);
        } else {
            this.setGuiOffsetX(0);
        }

        super.tick();
    }

    @Override
    public boolean mouseClickedRelative(int relativeClickX, int relativeClickY, int clickButton) {
        // All generic buttons
        if(this.goBackButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            Minecraft.getInstance().setScreen(new DesignerTabScreen());
            return true;
        } else if(this.pencilToolButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            this.currentTool = EEditorTool.PENCIL;
            return true;
        } else if(this.eraserToolButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            this.currentTool = EEditorTool.ERASER;
            return true;
        } else if(this.pickerToolButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            this.currentTool = EEditorTool.PICKER;
            return true;
        } else if(this.bucketToolButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            this.currentTool = EEditorTool.BUCKET;
            return true;
        } else if(this.colorEditorToolButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            this.currentTool = EEditorTool.COLOR_EDITOR;
            this.editedColorSlot = this.mainColorSlotGadget;
            return true;
        } else if(this.lessOpacityButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            this.editorCanvas.setOpacity(this.editorCanvas.getOpacity() - 0.25F);
            return true;
        } else if(this.moreOpacityButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            this.editorCanvas.setOpacity(this.editorCanvas.getOpacity() + 0.25F);
            return true;
        }

        // Interactions with color palette
        if(clickButton == 0 || clickButton == 1) {
            ColorSlotGadget clickedColorSlot = null;

            for(ColorSlotGadget colorSlotGadget : this.colorPaletteSlotGadgets) {
                if(colorSlotGadget.isMouseOver(relativeClickX, relativeClickY)) {
                    clickedColorSlot = colorSlotGadget;
                    break;
                }
            }

            if(clickedColorSlot != null && this.currentTool != EEditorTool.BUCKET && this.currentTool != EEditorTool.NONE &&
                    this.currentTool != EEditorTool.COLOR_EDITOR) {
                playColorSlotSound();
                switch(this.currentTool) {
                    case PENCIL:
                        clickedColorSlot.color = (clickButton == 0 ? this.mainColorSlotGadget.color : this.secondaryColorSlotGadget.color);
                        return true;
                    case ERASER:
                        clickedColorSlot.color = FastColor.ARGB32.color(0x00, 0x00, 0x00, 0x00);
                        return true;
                    case PICKER:
                        (clickButton == 0 ? this.mainColorSlotGadget : this.secondaryColorSlotGadget).color = clickedColorSlot.color;
                        return true;
                }
            }
        }

        // Interactions that edit and select the colors
        if(this.currentTool == EEditorTool.COLOR_EDITOR && (clickButton == 0 || clickButton == 1)) {
            ColorSlotGadget selectedColorSlot = null;

            if(this.mainColorSlotGadget.isMouseOver(relativeClickX, relativeClickY)) {
                selectedColorSlot = this.mainColorSlotGadget;
            } else if(this.secondaryColorSlotGadget.isMouseOver(relativeClickX, relativeClickY)) {
                selectedColorSlot = this.secondaryColorSlotGadget;
            } else {
                for(ColorSlotGadget colorSlotGadget : this.colorPaletteSlotGadgets) {
                    if(colorSlotGadget.isMouseOver(relativeClickX, relativeClickY)) {
                        selectedColorSlot = colorSlotGadget;
                        break;
                    }
                }
            }

            if(selectedColorSlot != null) {
                playColorSlotSound();
                if(this.editedColorSlot != null) {
                    this.editedColorSlot.drawTarget = false;
                }
                selectedColorSlot.drawTarget = true;
                this.editedColorSlot = selectedColorSlot;
                this.colorEditorScreen.handleColorEditorValueChange(
                        this.editedColorSlot.getR(), this.editedColorSlot.getG(),
                        this.editedColorSlot.getB(), this.editedColorSlot.getA(),
                        true, true);
            }
        }

        // Interactions with canvas
        if(this.editorCanvas.isModifiable && this.editorCanvas.isMouseOver(relativeClickX, relativeClickY)) {
            if(this.currentTool != EEditorTool.NONE && this.currentTool != EEditorTool.COLOR_EDITOR) {
                if(this.handleCanvasDrawingAction(relativeClickX, relativeClickY, clickButton)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean mouseDraggedRelative(int newRelativePosX, int newRelativePosY, int clickButton, double deltaX, double deltaY) {
        if(this.editorCanvas.isModifiable && this.editorCanvas.isMouseOver(newRelativePosX, newRelativePosY)) {
            if(this.currentTool != EEditorTool.BUCKET && this.currentTool != EEditorTool.NONE) {
                if(this.handleCanvasDrawingAction(newRelativePosX, newRelativePosY, clickButton)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handleCanvasDrawingAction(int relativeMouseX, int relativeMouseY, int clickButton) {
        // We assume that the position is within the canvas
        switch(this.currentTool) {
            case PENCIL:
                return this.editorCanvas.setColor(relativeMouseX, relativeMouseY,
                        (clickButton == 0 ? this.mainColorSlotGadget.color : this.secondaryColorSlotGadget.color));
            case ERASER:
                return this.editorCanvas.setColor(relativeMouseX, relativeMouseY,
                        FastColor.ARGB32.color(0x00, 0x00, 0x00, 0x00));
            case PICKER:
                (clickButton == 0 ? this.mainColorSlotGadget : this.secondaryColorSlotGadget).color = this.editorCanvas.getColor(
                        relativeMouseX, relativeMouseY);
                return true;
        }

        // We did not make modifications to the canvas.
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
        BUCKET,
        COLOR_EDITOR;
    }
}
