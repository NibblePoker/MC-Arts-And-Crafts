package com.nibblepoker.artsandcrafts.interfaces;

import com.nibblepoker.artsandcrafts.interfaces.components.NPGadget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Extension of the standard "Screen" class that provides a couple more functions with relative positions.
 * There might be a native way to achieve this, but I couldn't find it and I couldn't be bothered to.
 */
public abstract class NPScreen extends Screen {
    private int guiWidth, guiHeight;

    protected ArrayList<NPGadget> gadgets;

    protected NPScreen(Component title, int guiWidth, int guiHeight) {
        super(title);
        this.guiWidth = guiWidth;
        this.guiHeight = guiHeight;
        this.gadgets = new ArrayList<>();
    }

    /* New features */

    protected void addGadgets(NPGadget... gadgets) {
        this.gadgets.addAll(Arrays.asList(gadgets));
    }

    public void resizeGui(int guiWidth, int guiHeight) {
        int oldGuiWidth = this.guiWidth;
        int oldGuiHeight = this.guiHeight;
        this.guiWidth = guiWidth;
        this.guiHeight = guiHeight;
        this.onGuiResize(oldGuiWidth, oldGuiHeight, this.guiWidth, this.guiHeight);
    }

    public void onGuiResize(int oldGuiWidth, int oldGuiHeight, int newGuiWidth, int newGuiHeight) {
        this.gadgets.forEach(gadget -> gadget.onParentGuiResize(oldGuiWidth, oldGuiHeight, newGuiWidth, newGuiHeight));
    }

    /* Overrides & extensions */

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.render(graphics, mouseX, mouseY, partialTick, (this.width - this.guiWidth) / 2, (this.height - this.guiHeight) / 2);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int originX, int originY) {
        this.renderRelative(graphics, partialTick, originX, originY, mouseX - originX, mouseY - originY);
    }

    public void renderRelative(GuiGraphics graphics, float partialTick, int originX, int originY, int relativeMouseX, int relativeMouseY) {
        this.renderBackground(graphics);
        for(NPGadget gadget : this.gadgets) {
            gadget.render(graphics, partialTick, relativeMouseX, relativeMouseY, originX, originY);
        }
    }

    @Override
    public boolean mouseClicked(double clickX, double clickY, int clickButton) {
        return this.mouseClickedRelative(
                (int) clickX - ((this.width - this.guiWidth) / 2),
                (int) clickY - ((this.height - this.guiHeight) / 2),
                clickButton);
    }

    public boolean mouseClickedRelative(int relativeClickX, int relativeClickY, int clickButton) {
        for(NPGadget gadget : this.gadgets) {
            if(gadget.mouseClicked(relativeClickX, relativeClickY, clickButton)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double newPosX, double newPosY, int clickButton, double deltaX, double deltaY) {
        return this.mouseDraggedRelative(
                (int) newPosX - ((this.width - this.guiWidth) / 2),
                (int) newPosY - ((this.height - this.guiHeight) / 2),
                clickButton, deltaX, deltaY);
    }

    public boolean mouseDraggedRelative(int newRelativePosX, int newRelativePosY, int clickButton, double deltaX, double deltaY) {
        return false;
    }

    // FIXME: Tick gadgets !

    /* Getters & setters */
    public int getGuiWidth() {
        return this.guiWidth;
    }

    public int getGuiHeight() {
        return this.guiHeight;
    }
}
