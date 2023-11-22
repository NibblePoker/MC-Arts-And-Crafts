package com.nibblepoker.artsandcrafts.interfaces.components;

public enum EArtButtonType {
    UP_SMALL(10, 10, 0, 0),
    DOWN_SMALL(10, 10, 10, 0),
    LEFT_MEDIUM(10, 14, 0, 32),
    RIGHT_MEDIUM(10, 14, 10, 32),
    RIGHT_LARGE(20, 20, 48, 0),
    LEFT_LARGE(20, 20, 68, 0);

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
