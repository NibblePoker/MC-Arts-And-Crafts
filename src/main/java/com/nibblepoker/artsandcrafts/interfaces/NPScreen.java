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

    private int guiOffsetX, guiOffsetY;

    private boolean isEnabled;

    protected final ArrayList<NPGadget> gadgets;

    protected final ArrayList<NPScreen> subScreens;

    protected NPScreen(Component title, int guiWidth, int guiHeight) {
        super(title);
        this.guiWidth = guiWidth;
        this.guiHeight = guiHeight;
        this.gadgets = new ArrayList<>();
        this.subScreens = new ArrayList<>();
        this.guiOffsetX = 0;
        this.guiOffsetY = 0;
        this.isEnabled = true;
    }

    /* New features */

    protected void addGadgets(NPGadget... gadgets) {
        this.gadgets.addAll(Arrays.asList(gadgets));
    }

    protected void addSubScreens(NPScreen... subScreen) {
        this.subScreens.addAll(Arrays.asList(subScreen));
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
        this.render(graphics,
                mouseX,
                mouseY,
                partialTick,
                ((this.width - this.guiWidth) / 2) + this.guiOffsetX,
                ((this.height - this.guiHeight) / 2) + this.guiOffsetY);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    /** DO NOT OVERRIDE !  WILL BE REMOVED SOON(tm) */
    private void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int originX, int originY) {
        this.renderRelative(graphics, partialTick, originX, originY,
                mouseX - originX,
                mouseY - originY);
    }

    public void renderRelative(GuiGraphics graphics, float partialTick, int originX, int originY, int relativeMouseX, int relativeMouseY) {
        this.renderBackground(graphics);
        for(NPScreen subScreen : this.subScreens) {
            if(subScreen.isEnabled) {
                subScreen.renderRelative(graphics, partialTick,
                        originX + subScreen.guiOffsetX, originY + subScreen.guiOffsetY,
                        relativeMouseX - subScreen.guiOffsetX, relativeMouseY - subScreen.guiOffsetY);
            }
        }
        for(NPGadget gadget : this.gadgets) {
            gadget.render(graphics, partialTick, relativeMouseX, relativeMouseY, originX, originY);
        }
    }

    @Override
    public boolean mouseClicked(double clickX, double clickY, int clickButton) {
        if(this.mouseClickedRelative(
                (int) clickX - ((this.width - this.guiWidth) / 2) - this.guiOffsetX,
                (int) clickY - ((this.height - this.guiHeight) / 2) - this.guiOffsetY,
                clickButton)) {
            return true;
        }

        for(NPScreen subScreen : this.subScreens) {
            if(subScreen.mouseClickedRelative(
                    (int) clickX - ((this.width - this.guiWidth) / 2) - (this.guiOffsetX + subScreen.guiOffsetX),
                    (int) clickY - ((this.height - this.guiHeight) / 2) - (this.guiOffsetY + subScreen.guiOffsetY),
                    clickButton)) {
                return true;
            }
        }

        return false;
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
        if(this.mouseDraggedRelative(
                (int) newPosX - ((this.width - this.guiWidth) / 2) - this.guiOffsetX,
                (int) newPosY - ((this.height - this.guiHeight) / 2) - this.guiOffsetY,
                clickButton, deltaX, deltaY)) {
            return true;
        }

        for(NPScreen subScreen : this.subScreens) {
            if(subScreen.mouseDraggedRelative(
                    (int) newPosX - ((this.width - this.guiWidth) / 2) - (this.guiOffsetX + subScreen.guiOffsetX),
                    (int) newPosY - ((this.height - this.guiHeight) / 2) - (this.guiOffsetY + subScreen.guiOffsetY),
                    clickButton, deltaX, deltaY)) {
                return true;
            }
        }

        return false;
    }

    public boolean mouseDraggedRelative(int newRelativePosX, int newRelativePosY, int clickButton, double deltaX, double deltaY) {
        for(NPGadget gadget : this.gadgets) {
            if(gadget.mouseDragged(newRelativePosX, newRelativePosY, clickButton, deltaX, deltaY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double clickX, double clickY, int clickButton) {
        if(this.mouseReleasedRelative(
                (int) clickX - ((this.width - this.guiWidth) / 2) - this.guiOffsetX,
                (int) clickY - ((this.height - this.guiHeight) / 2) - this.guiOffsetY,
                clickButton)) {
            return true;
        }

        for(NPScreen subScreen : this.subScreens) {
            if(subScreen.mouseReleasedRelative(
                    (int) clickX - ((this.width - this.guiWidth) / 2) - (this.guiOffsetX + subScreen.guiOffsetX),
                    (int) clickY - ((this.height - this.guiHeight) / 2) - (this.guiOffsetY + subScreen.guiOffsetY),
                    clickButton)) {
                return true;
            }
        }

        return false;
    }

    public boolean mouseReleasedRelative(int relativeClickX, int relativeClickY, int clickButton) {
        for(NPGadget gadget : this.gadgets) {
            if(gadget.mouseReleased(relativeClickX, relativeClickY, clickButton)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean charTyped(char p_94732_, int p_94733_) {
        for(NPGadget gadget : this.gadgets) {
            if(gadget.charTyped(p_94732_, p_94733_)) {
                return true;
            }
        }
        for(NPScreen subScreen : this.subScreens) {
            if(subScreen.isEnabled) {
                if(subScreen.charTyped(p_94732_, p_94733_)) {
                    return true;
                }
            }
        }
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

    public int getGuiOffsetX() {
        return guiOffsetX;
    }

    public int getGuiOffsetY() {
        return guiOffsetY;
    }

    public void setGuiOffsetX(int guiOffsetX) {
        this.guiOffsetX = guiOffsetX;
    }

    public void setGuiOffsetY(int guiOffsetY) {
        this.guiOffsetY = guiOffsetY;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
