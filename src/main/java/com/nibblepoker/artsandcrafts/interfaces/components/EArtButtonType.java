package com.nibblepoker.artsandcrafts.interfaces.components;

/**
 * @version 1.0.0
 */
public enum EArtButtonType {
    UP_SMALL(10, 10, 0, 0),
    DOWN_SMALL(10, 10, 10, 0),
    LEFT_MEDIUM(10, 14, 0, 32),
    RIGHT_MEDIUM(10, 14, 10, 32),
    RIGHT_LARGE(20, 20, 48, 0),
    LEFT_LARGE(20, 20, 68, 0),
    EDITOR_PLUS(12, 13, 0, 80),
    EDITOR_MINUS(12, 13, 12, 80),
    EDITOR_OPACITY_MINUS(12, 13, 24, 80),
    EDITOR_OPACITY_PLUS(12, 13, 36, 80),
    EDITOR_TOOL_PENCIL(14, 15, 14, 128),
    EDITOR_TOOL_ERASER(14, 15, 0, 128),
    EDITOR_TOOL_PICKER(14, 15, 28, 128),
    EDITOR_TOOL_BUCKET(14, 15, 56, 128);

    public final int width;
    public final int height;
    public final int textureOriginX;
    public final int textureOriginY;

    EArtButtonType(int width, int height, int textureOriginX, int textureOriginY) {
        this.width = width;
        this.height = height;
        this.textureOriginX = textureOriginX;
        this.textureOriginY = textureOriginY;
    }
}
