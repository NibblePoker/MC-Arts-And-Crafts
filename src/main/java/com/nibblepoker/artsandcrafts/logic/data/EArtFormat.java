package com.nibblepoker.artsandcrafts.logic.data;

public enum EArtFormat {
    // Management formats (0x00000000 - 0x000000FF)
    UNKNOWN_FORMAT(0x00000000, EArtResolution.FULL_1X1, EArtColorDepth.RGBA, 16*16*4),
    UNKNOWN_UNCACHED(0x00000000, EArtResolution.FULL_1X1, EArtColorDepth.RGBA, 16*16*4),
    ERROR(0x00000001, EArtResolution.FULL_1X1, EArtColorDepth.RGBA, 16*16*4),
    LOADING(0x00000002, EArtResolution.FULL_1X1, EArtColorDepth.RGBA, 16*16*4),

    //BANNED(0x00000002),

    ART_FULL_1X1_RGBA(0xc5f13ab8, EArtResolution.FULL_1X1, EArtColorDepth.RGBA, 16*16*4);

    private final int formatCode;
    private final EArtResolution resolution;
    private final EArtColorDepth colorDepth;
    public final int newArtDataSize;

    public int getFormatCode() {
        return formatCode;
    }

    EArtFormat(int formatCode, EArtResolution resolution, EArtColorDepth colorDepth, int newArtDataSize) {
        this.formatCode = formatCode;
        this.resolution = resolution;
        this.colorDepth = colorDepth;
        this.newArtDataSize = newArtDataSize;
    }

    public static EArtFormat getFromCode(int formatCode) {
        for(EArtFormat artFormat : EArtFormat.values()) {
            if(artFormat.getFormatCode() == formatCode) {
                return artFormat;
            }
        }
        return EArtFormat.UNKNOWN_FORMAT;
    }

    public boolean isManagementFormat() {
        //return this.formatCode <= 0x000000FF; // FIXME: doesn't work, IDK why, it just doesn't...
        return this.formatCode == UNKNOWN_FORMAT.formatCode ||
                this.formatCode == UNKNOWN_UNCACHED.formatCode ||
                this.formatCode == ERROR.formatCode ||
                this.formatCode == LOADING.formatCode;
    }

    public enum EArtResolution {
        FULL_1X1;
    }

    public enum EArtColorDepth {
        RGBA,
        MONOCHROME;
    }
}
