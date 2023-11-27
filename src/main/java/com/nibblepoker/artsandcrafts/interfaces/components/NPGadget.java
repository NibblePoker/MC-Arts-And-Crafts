package com.nibblepoker.artsandcrafts.interfaces.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class NPGadget {
    protected int width, height;

    /**
     * Offsets relative to the parent screen's origin.
     */
    protected int offsetX, offsetY;

    protected ArrayList<Component> tooltipText;

    public NPGadget(int width, int height, int offsetX, int offsetY) {
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void addTooltipComponents(Component... components) {
        if(this.tooltipText == null) {
            this.tooltipText = new ArrayList<>();
        }
        this.tooltipText.addAll(Arrays.asList(components));
    }

    public void render(GuiGraphics graphics, float partialTick, int parentMouseX, int parentMouseY, int parentOriginX, int parentOriginY) {
        this.renderRelative(graphics, partialTick,
                parentMouseX - this.offsetX, parentMouseY - this.offsetY,
                parentOriginX + this.offsetX, parentOriginY + this.offsetY);
    }

    public void renderRelative(GuiGraphics graphics, float partialTick, int relativeMouseX, int relativeMouseY,
                               int relativeOriginX, int relativeOriginY) {
        // Don't forget to call 'super.renderRelative' to get tooltips if you override this function.
        if(this.tooltipText != null) {
            if(!this.tooltipText.isEmpty() && this.isMouseOverRelative(relativeMouseX, relativeMouseY)) {
                graphics.renderComponentTooltip(Minecraft.getInstance().font, this.tooltipText,
                        relativeOriginX, relativeOriginY);
            }
        }
    }

    public void onParentGuiResize(int oldGuiWidth, int oldGuiHeight, int newGuiWidth, int newGuiHeight) {
        // Override this if the gadget also needs to resize itself.
    }

    public boolean mouseClicked(int parentMouseX, int parentMouseY, int clickButton) {
        return this.mouseClickedRelative(parentMouseX - this.offsetX, parentMouseY - this.offsetY, clickButton);
    }

    public boolean mouseClickedRelative(int relativeMouseX, int relativeMouseY, int clickButton) {
        // Override this if the gadget handles non-propagating clicks.
        return false;
    }

    public boolean mouseReleased(int parentMouseX, int parentMouseY, int clickButton) {
        return this.mouseReleasedRelative(parentMouseX - this.offsetX, parentMouseY - this.offsetY, clickButton);
    }

    public boolean mouseReleasedRelative(int relativeMouseX, int relativeMouseY, int clickButton) {
        // Override this if the gadget handles non-propagating clicks.
        return false;
    }

    public boolean mouseDragged(int parentMouseX, int parentMouseY, int clickButton, double deltaX, double deltaY) {
        return this.mouseDraggedRelative(parentMouseX - this.offsetX, parentMouseY - this.offsetY,
                clickButton, deltaX, deltaY);
    }

    public boolean mouseDraggedRelative(int relativeMouseX, int relativeMouseY, int clickButton, double deltaX, double deltaY) {
        // Override this if the gadget handles non-propagating dragged clicks.
        return false;
    }

    public boolean isMouseOver(int parentMouseX, int parentMouseY) {
        return this.isMouseOverRelative(parentMouseX - this.offsetX,parentMouseY - this.offsetY);
    }

    public boolean isMouseOverRelative(int relativeMouseX, int relativeMouseY) {
        return relativeMouseX >= 0 && relativeMouseX < this.width && relativeMouseY >= 0 && relativeMouseY < this.height;
    }

    public void tick() {
        // Override this if the gadget needs to update internal stuff outside of events.
    }

    public boolean charTyped(char charTyped, int idk01) {
        return false;
    }
}