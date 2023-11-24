package com.nibblepoker.artsandcrafts.interfaces.components;

/**
 * @version 1.0.0
 */
public enum EColorSlotGadgetType {
    LARGE(18, 18),
    SMALL(13, 13);

    public final int width;
    public final int height;

    EColorSlotGadgetType(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
