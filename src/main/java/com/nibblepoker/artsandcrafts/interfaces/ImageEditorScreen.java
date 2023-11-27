package com.nibblepoker.artsandcrafts.interfaces;

import com.mojang.blaze3d.platform.InputConstants;
import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.interfaces.components.*;
import com.nibblepoker.artsandcrafts.logic.data.ArtData;
import com.nibblepoker.artsandcrafts.logic.data.EArtFormat;
import com.nibblepoker.artsandcrafts.utils.ImageUtils;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;

public class ImageEditorScreen extends NPScreen {
    private final static Component textTools = Component.translatable(
            "text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.editor.tools");
    private final static Component textColors = Component.translatable(
            "text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.editor.colors");
    private final static Component textOptions = Component.translatable(
            "text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.editor.options");

    private final ColorEditorSideScreen colorEditorScreen;

    private final ArtButtonGadget goBackButton;
    private final TextButtonGadget saveButton;

    private final CanvasGadget editorCanvas;

    private final ArtButtonGadget zoomOutButton, zoomInButton;
    private final ArtButtonGadget lessOpacityButton, moreOpacityButton;

    private final ArtButtonGadget pencilToolButton, eraserToolButton, pickerToolButton, bucketToolButton, colorEditorToolButton;

    private EEditorTool currentTool = EEditorTool.NONE;

    private final ColorSlotGadget mainColorSlotGadget, secondaryColorSlotGadget;
    private final ColorSlotGadget[] colorPaletteSlotGadgets = new ColorSlotGadget[20];

    // Other variables
    private ColorSlotGadget editedColorSlot = null;

    private final ArtData editedImageReference;
    private boolean canSaveImageAgain;

    /**
     * This constructor should only be used for debugging !
     */
    protected ImageEditorScreen() {
        this(new ArtData(EArtFormat.ART_FULL_1X1_RGBA));
    }

    protected ImageEditorScreen(ArtData editedImageDraft) {
        super(Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.title.editor"),
                240, 185);

        this.editedImageReference = editedImageDraft;
        this.canSaveImageAgain = true;

        this.colorEditorScreen = new ColorEditorSideScreen();
        this.colorEditorScreen.setGuiOffsetX(this.getGuiWidth() - 4);
        this.colorEditorScreen.setGuiOffsetY(32);
        this.colorEditorScreen.setEnabled(false);
        this.addSubScreens(this.colorEditorScreen);

        TabGadget headerTabGadget = new TabGadget(
                0, 0, this.getGuiWidth(), 24,
                TabGadget.TabOrientation.NONE, 9);
        TabGadget bodyTabGadget = new TabGadget(
                4, 24, this.getGuiWidth() - 8, this.getGuiHeight() - 24,
                TabGadget.TabOrientation.DOWN, 10);

        this.goBackButton = new ArtButtonGadget(EArtButtonType.LEFT_MEDIUM, 5, 5);

        this.saveButton = new TextButtonGadget( this.getGuiWidth() - 48 - 6, 5, 48, 14,
                Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.editor.save"));

        this.editorCanvas = new CanvasGadget(16, 32, 128, 128,
                ImageUtils.bytesToNativeImage(16, 16, this.editedImageReference.getImageData()));
        this.editorCanvas.isModifiable = true;

        this.zoomOutButton = new ArtButtonGadget(EArtButtonType.EDITOR_MINUS, 15, 165);
        this.zoomOutButton.isDisabled = true;
        this.zoomInButton = new ArtButtonGadget(EArtButtonType.EDITOR_PLUS, 43, 165);
        this.zoomInButton.isDisabled = true;

        this.lessOpacityButton = new ArtButtonGadget(EArtButtonType.EDITOR_OPACITY_MINUS, 91, 165);
        this.moreOpacityButton = new ArtButtonGadget(EArtButtonType.EDITOR_OPACITY_PLUS, 133, 165);

        this.pencilToolButton = new ArtButtonGadget(EArtButtonType.EDITOR_TOOL_PENCIL, 151, 40);
        //pencilToolButton.addTooltipComponents(Component.literal("Test123"));

        this.eraserToolButton = new ArtButtonGadget(EArtButtonType.EDITOR_TOOL_ERASER, 167, 40);
        this.pickerToolButton = new ArtButtonGadget(EArtButtonType.EDITOR_TOOL_PICKER, 183, 40);
        this.bucketToolButton = new ArtButtonGadget(EArtButtonType.EDITOR_TOOL_BUCKET, 199, 40);
        this.colorEditorToolButton = new ArtButtonGadget(EArtButtonType.EDITOR_TOOL_COLOR_FULL, 215, 40);

        this.mainColorSlotGadget = new ColorSlotGadget(152, 71, EColorSlotGadgetType.LARGE,
                FastColor.ARGB32.color(0xFF, 0xFF, 0xFF, 0xFF));
        this.secondaryColorSlotGadget = new ColorSlotGadget(160, 79, EColorSlotGadgetType.LARGE,
                FastColor.ARGB32.color(0xFF, 0x00, 0x00, 0x00));
        for(int i = 0; i < this.colorPaletteSlotGadgets.length; i++) {
            int startX = 151 + ((i % 5) * 16);
            int startY = 101 + ((i / 5) * 16);
            this.colorPaletteSlotGadgets[i] = new ColorSlotGadget(startX, startY, EColorSlotGadgetType.SMALL);
        }

        // Palette "ENDESGA SOFT 16 PALETTE" by ENDESGA on lospec.
        // See: https://lospec.com/palette-list/endesga-soft-16
        this.colorPaletteSlotGadgets[0].color = FastColor.ARGB32.color(0xFF, 0xfe, 0xfe, 0xd7);
        this.colorPaletteSlotGadgets[1].color = FastColor.ARGB32.color(0xFF, 0xdb, 0xbc, 0x96);
        this.colorPaletteSlotGadgets[2].color = FastColor.ARGB32.color(0xFF, 0xdd, 0xac, 0x46);
        this.colorPaletteSlotGadgets[3].color = FastColor.ARGB32.color(0xFF, 0xc2, 0x59, 0x40);
        this.colorPaletteSlotGadgets[4].color = FastColor.ARGB32.color(0xFF, 0x68, 0x3d, 0x64);
        this.colorPaletteSlotGadgets[5].color = FastColor.ARGB32.color(0xFF, 0x9c, 0x66, 0x59);
        this.colorPaletteSlotGadgets[6].color = FastColor.ARGB32.color(0xFF, 0x88, 0x43, 0x4f);
        this.colorPaletteSlotGadgets[7].color = FastColor.ARGB32.color(0xFF, 0x4d, 0x28, 0x31);
        this.colorPaletteSlotGadgets[8].color = FastColor.ARGB32.color(0xFF, 0xa9, 0xab, 0xa3);
        this.colorPaletteSlotGadgets[9].color = FastColor.ARGB32.color(0xFF, 0x66, 0x68, 0x69);
        this.colorPaletteSlotGadgets[10].color = FastColor.ARGB32.color(0xFF, 0x51, 0xb1, 0xca);
        this.colorPaletteSlotGadgets[11].color = FastColor.ARGB32.color(0xFF, 0x17, 0x73, 0xb8);
        this.colorPaletteSlotGadgets[12].color = FastColor.ARGB32.color(0xFF, 0x63, 0x9f, 0x5b);
        this.colorPaletteSlotGadgets[13].color = FastColor.ARGB32.color(0xFF, 0x37, 0x6e, 0x49);
        this.colorPaletteSlotGadgets[14].color = FastColor.ARGB32.color(0xFF, 0x32, 0x34, 0x41);
        this.colorPaletteSlotGadgets[15].color = FastColor.ARGB32.color(0xFF, 0x16, 0x13, 0x23);

        this.addGadgets(
                headerTabGadget, bodyTabGadget, this.goBackButton, this.editorCanvas, this.lessOpacityButton,
                this.moreOpacityButton, this.pencilToolButton, this.eraserToolButton, this.pickerToolButton,
                this.bucketToolButton, this.colorEditorToolButton, this.secondaryColorSlotGadget, this.mainColorSlotGadget,
                this.zoomOutButton, this.zoomInButton, this.saveButton);
        this.addGadgets(this.colorPaletteSlotGadgets);
    }

    @Override
    public void renderRelative(GuiGraphics graphics, float partialTick, int originX, int originY, int relativeMouseX, int relativeMouseY) {
        // Rendering background, gadgets & sub-screens
        super.renderRelative(graphics, partialTick, originX, originY, relativeMouseX, relativeMouseY);

        // Rendering text
        ScreenUtils.drawShadedCenteredString(graphics, Minecraft.getInstance().font, this.title,
                originX + ((this.getGuiWidth() - 32) / 2), originY + 12, ScreenUtils.COLOR_WHITE);

        graphics.drawString(Minecraft.getInstance().font, textTools,
                originX + 150, originY + 29, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, textColors,
                originX + 150, originY + 60, ScreenUtils.COLOR_WHITE);

        graphics.drawString(Minecraft.getInstance().font, "1x",
                originX + 29, originY + 168, ScreenUtils.COLOR_WHITE);

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
        if(this.saveButton.mouseClicked(relativeClickX, relativeClickY, clickButton) ||
                this.goBackButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
            this.saveImageAsDraft();
            this.canSaveImageAgain = false;
            if(this.saveButton.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
                playSavedSound();
                Minecraft.getInstance().setScreen(new ImagePreviewScreen(this.editedImageReference, true));
            } else {
                Minecraft.getInstance().setScreen(new DesignerTabScreen());
            }
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
            this.editedColorSlot.drawTarget = true;
            this.colorEditorScreen.handleColorEditorValueChange(
                    this.editedColorSlot.getR(), this.editedColorSlot.getG(),
                    this.editedColorSlot.getB(), this.editedColorSlot.getA(),
                    true, true);
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

    private static void playSavedSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.0F));
    }

    private enum EEditorTool {
        NONE,
        PENCIL,
        ERASER,
        PICKER,
        BUCKET,
        COLOR_EDITOR;
    }

    @Override
    public void onClose() {
        // Triggered if ESC is pressed.
        //ArtsAndCraftsMod.LOGGER.debug("Image editor's 'onClose' called !");
        //this.saveImageAsDraft();
        //this.canSaveImageAgain = false;
        super.onClose();
    }

    @Override
    public void removed() {
        // Triggered if the active screen changes.
        ArtsAndCraftsMod.LOGGER.trace("Image editor's 'removed' called !");
        this.saveImageAsDraft();
        this.canSaveImageAgain = false;
        super.removed();
    }

    private void saveImageAsDraft() {
        if(this.canSaveImageAgain) {
            ArtsAndCraftsMod.LOGGER.debug("Saving editor's image as draft image !");
            this.editedImageReference.setImageData(ImageUtils.nativeImageToBytes(
                    this.editorCanvas.getImage()), false);
        }
    }
}
