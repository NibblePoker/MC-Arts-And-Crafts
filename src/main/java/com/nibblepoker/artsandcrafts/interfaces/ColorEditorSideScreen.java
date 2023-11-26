package com.nibblepoker.artsandcrafts.interfaces;

import com.nibblepoker.artsandcrafts.ArtsAndCraftsMod;
import com.nibblepoker.artsandcrafts.interfaces.components.*;
import com.nibblepoker.artsandcrafts.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * The code in this class sucks, sorry, tough shit.
 * It works and that's what matters here.
 */
public class ColorEditorSideScreen extends NPScreen {
    private final static Component TEXT_RGB = Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.color_edit.rgb");
    private final static Component TEXT_R = Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.color_edit.r");
    private final static Component TEXT_G = Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.color_edit.g");
    private final static Component TEXT_B = Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.color_edit.b");
    private final static Component TEXT_ALPHA = Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.color_edit.alpha");
    private final static Component TEXT_A = Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.color_edit.a");

    private final SliderGadget redSliderGadget, greenSliderGadget, blueSliderGadget, alphaSliderGadget;
    private final TextInputGadget redValueInputGadget, greenValueInputGadget, blueValueInputGadget, alphaValueInputGadget;

    private int colorR, colorG, colorB, colorA;

    protected ColorEditorSideScreen() {
        super(Component.translatable("text." + ArtsAndCraftsMod.MOD_ID + ".designer_tab.title.color_edit"),
                152, 104);

        TabGadget backgroundTabGadget = new TabGadget(0, 0, this.getGuiWidth(), this.getGuiHeight(),
                TabGadget.TabOrientation.RIGHT, 10);

        this.redSliderGadget = new SliderGadget(64, 17, 15, 4, 5) {
            @Override
            public void handleValueChange(int relativeMouseX, int relativeMouseY) {
                super.handleValueChange(relativeMouseX, relativeMouseY);
                handleColorEditorValueChange(this.getMappedValue(0, 255), colorG, colorB, colorA, false, true);
            }
        };
        this.greenSliderGadget = new SliderGadget(64, 17, 33, 21, 20) {
            @Override
            public void handleValueChange(int relativeMouseX, int relativeMouseY) {
                super.handleValueChange(relativeMouseX, relativeMouseY);
                handleColorEditorValueChange(colorR, this.getMappedValue(0, 255), colorB, colorA, false, true);
            }
        };
        this.blueSliderGadget = new SliderGadget(64, 17, 51, 10, 11) {
            @Override
            public void handleValueChange(int relativeMouseX, int relativeMouseY) {
                super.handleValueChange(relativeMouseX, relativeMouseY);
                handleColorEditorValueChange(colorR, colorG, this.getMappedValue(0, 255), colorA, false, true);
            }
        };
        this.alphaSliderGadget = new SliderGadget(64, 17, 82, 39, 38) {
            @Override
            public void handleValueChange(int relativeMouseX, int relativeMouseY) {
                super.handleValueChange(relativeMouseX, relativeMouseY);
                handleColorEditorValueChange(colorR, colorG, colorB, this.getMappedValue(0, 255), false, true);
            }
        };

        this.redValueInputGadget = new TextInputGadget(32, 15, 102, 15) {
            @Override
            public boolean onPreCharTyped(char charTyped, int unknown) {
                return "0123456789".indexOf(charTyped) != -1;
            }
            @Override
            public boolean onPostCharTyped(char charTyped, int unknown) {
                this.content = String.valueOf(Math.max(0, Math.min(255, Integer.parseInt(this.content))));
                handleColorEditorValueChange(Integer.parseInt(this.content), colorG, colorB, colorA, true, false);
                return true;
            }
        };
        this.greenValueInputGadget = new TextInputGadget(32, 15, 102, 33) {
            @Override
            public boolean onPreCharTyped(char charTyped, int unknown) {
                return "0123456789".indexOf(charTyped) != -1;
            }
            @Override
            public boolean onPostCharTyped(char charTyped, int unknown) {
                this.content = String.valueOf(Math.max(0, Math.min(255, Integer.parseInt(this.content))));
                handleColorEditorValueChange(colorR, Integer.parseInt(this.content), colorB, colorA, true, false);
                return true;
            }
        };
        this.blueValueInputGadget = new TextInputGadget(32, 15, 102, 51) {
            @Override
            public boolean onPreCharTyped(char charTyped, int unknown) {
                return "0123456789".indexOf(charTyped) != -1;
            }
            @Override
            public boolean onPostCharTyped(char charTyped, int unknown) {
                this.content = String.valueOf(Math.max(0, Math.min(255, Integer.parseInt(this.content))));
                handleColorEditorValueChange(colorR, colorG, Integer.parseInt(this.content), colorA, true, false);
                return true;
            }
        };
        this.alphaValueInputGadget = new TextInputGadget(32, 15, 102, 82) {
            @Override
            public boolean onPreCharTyped(char charTyped, int unknown) {
                return "0123456789".indexOf(charTyped) != -1;
            }
            @Override
            public boolean onPostCharTyped(char charTyped, int unknown) {
                this.content = String.valueOf(Math.max(0, Math.min(255, Integer.parseInt(this.content))));
                handleColorEditorValueChange(colorR, colorG, colorB, Integer.parseInt(this.content), true, false);
                return true;
            }
        };

        ArtButtonGadget minusRedButton = new ArtButtonGadget(EArtButtonType.EDITOR_MINUS, 87, 16) {
            @Override
            public boolean mouseClickedRelative(int relativeMouseX, int relativeMouseY, int clickButton) {
                if(super.mouseClickedRelative(relativeMouseX, relativeMouseY, clickButton)) {
                    colorR = Math.max(0, colorR - 1);
                    handleColorEditorValueChange();
                    return true;
                }
                return false;
            }
        };
        ArtButtonGadget plusRedButton = new ArtButtonGadget(EArtButtonType.EDITOR_PLUS, 135, 16) {
            @Override
            public boolean mouseClickedRelative(int relativeMouseX, int relativeMouseY, int clickButton) {
                if(super.mouseClickedRelative(relativeMouseX, relativeMouseY, clickButton)) {
                    colorR = Math.min(255, colorR + 1);
                    handleColorEditorValueChange();
                    return true;
                }
                return false;
            }
        };

        ArtButtonGadget minusGreenButton = new ArtButtonGadget(EArtButtonType.EDITOR_MINUS, 87, 34) {
            @Override
            public boolean mouseClickedRelative(int relativeMouseX, int relativeMouseY, int clickButton) {
                if(super.mouseClickedRelative(relativeMouseX, relativeMouseY, clickButton)) {
                    colorG = Math.max(0, colorG - 1);
                    handleColorEditorValueChange();
                    return true;
                }
                return false;
            }
        };
        ArtButtonGadget plusGreenButton = new ArtButtonGadget(EArtButtonType.EDITOR_PLUS, 135, 34) {
            @Override
            public boolean mouseClickedRelative(int relativeMouseX, int relativeMouseY, int clickButton) {
                if(super.mouseClickedRelative(relativeMouseX, relativeMouseY, clickButton)) {
                    colorG = Math.min(255, colorG + 1);
                    handleColorEditorValueChange();
                    return true;
                }
                return false;
            }
        };

        ArtButtonGadget minusBlueButton = new ArtButtonGadget(EArtButtonType.EDITOR_MINUS, 87, 52) {
            @Override
            public boolean mouseClickedRelative(int relativeMouseX, int relativeMouseY, int clickButton) {
                if(super.mouseClickedRelative(relativeMouseX, relativeMouseY, clickButton)) {
                    colorB = Math.max(0, colorB - 1);
                    handleColorEditorValueChange();
                    return true;
                }
                return false;
            }
        };
        ArtButtonGadget plusBlueButton = new ArtButtonGadget(EArtButtonType.EDITOR_PLUS, 135, 52) {
            @Override
            public boolean mouseClickedRelative(int relativeMouseX, int relativeMouseY, int clickButton) {
                if(super.mouseClickedRelative(relativeMouseX, relativeMouseY, clickButton)) {
                    colorB = Math.min(255, colorB + 1);
                    handleColorEditorValueChange();
                    return true;
                }
                return false;
            }
        };

        ArtButtonGadget minusAlphaButton = new ArtButtonGadget(EArtButtonType.EDITOR_MINUS, 87, 83) {
            @Override
            public boolean mouseClickedRelative(int relativeMouseX, int relativeMouseY, int clickButton) {
                if(super.mouseClickedRelative(relativeMouseX, relativeMouseY, clickButton)) {
                    colorA = Math.max(0, colorA - 1);
                    handleColorEditorValueChange();
                    return true;
                }
                return false;
            }
        };
        ArtButtonGadget plusAlphaButton = new ArtButtonGadget(EArtButtonType.EDITOR_PLUS, 135, 83) {
            @Override
            public boolean mouseClickedRelative(int relativeMouseX, int relativeMouseY, int clickButton) {
                if(super.mouseClickedRelative(relativeMouseX, relativeMouseY, clickButton)) {
                    colorA = Math.min(255, colorA + 1);
                    handleColorEditorValueChange();
                    return true;
                }
                return false;
            }
        };

        // Setting a default color for testing
        this.handleColorEditorValueChange(0, 0, 0, 255, true, true);

        this.addGadgets(backgroundTabGadget,
                this.redValueInputGadget, this.greenValueInputGadget, this.blueValueInputGadget, this.alphaValueInputGadget,
                this.redSliderGadget, this.greenSliderGadget, this.blueSliderGadget, this.alphaSliderGadget,
                minusRedButton, plusRedButton, minusGreenButton, plusGreenButton, minusBlueButton, plusBlueButton,
                minusAlphaButton, plusAlphaButton);
    }

    @Override
    public void renderRelative(GuiGraphics graphics, float partialTick, int originX, int originY, int relativeMouseX, int relativeMouseY) {
        // Rendering background & gadgets (ONLY WHEN IT IS MAIN !!!)
        //super.renderRelative(graphics, partialTick, originX, originY, relativeMouseX, relativeMouseY);

        // Rendering gadgets manually
        for(NPGadget gadget : this.gadgets) {
            gadget.render(graphics, partialTick, relativeMouseX, relativeMouseY, originX, originY);
        }

        // Rendering text
        graphics.drawString(Minecraft.getInstance().font, TEXT_RGB, originX + 3, originY + 5, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, TEXT_R, originX + 6, originY + 18, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, TEXT_G, originX + 6, originY + 36, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, TEXT_B, originX + 6, originY + 54, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, TEXT_ALPHA, originX + 3, originY + 71, ScreenUtils.COLOR_WHITE);
        graphics.drawString(Minecraft.getInstance().font, TEXT_A, originX + 6, originY + 85, ScreenUtils.COLOR_WHITE);
    }

    private void handleColorEditorValueChange() {
        this.handleColorEditorValueChange(this.colorR, this.colorG, this.colorB, this.colorA, true, true);
    }

    public boolean handleColorEditorValueChange(int r, int g, int b, int a, boolean doSliders, boolean doInputs) {
        if(this.colorR == r && this.colorG == g && this.colorB == b && this.colorA == a && !(doSliders && doInputs)) {
            return false;
        }

        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
        this.colorA = a;

        // Eliminates jitter on sliders.
        if(doSliders) {
            this.redSliderGadget.setMappedValue(0, 255, r);
            this.greenSliderGadget.setMappedValue(0, 255, g);
            this.blueSliderGadget.setMappedValue(0, 255, b);
            this.alphaSliderGadget.setMappedValue(0, 255, a);
        }
        if(doInputs) {
            this.redValueInputGadget.content = String.valueOf(r);
            this.greenValueInputGadget.content = String.valueOf(g);
            this.blueValueInputGadget.content = String.valueOf(b);
            this.alphaValueInputGadget.content = String.valueOf(a);
        }

        return true;
    }
}
