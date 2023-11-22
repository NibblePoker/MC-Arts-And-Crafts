package com.nibblepoker.artsandcrafts.logic.data;

public enum EArtFormat {
    UNKNOWN_FORMAT(0x00000000),
    UNKNOWN_UNCACHED(0x00000000),
    ERROR(0x00000001),
    LOADING(0x00000002),

    //BANNED(0x00000002),

    ART_FULL_1X1(0xc5f13ab8);

    private final int formatCode;

    public int getFormatCode() {
        return formatCode;
    }

    EArtFormat(int formatCode) {
        this.formatCode = formatCode;
    }

    public static EArtFormat getFromCode(int formatCode) {
        for(EArtFormat artFormat : EArtFormat.values()) {
            if(artFormat.getFormatCode() == formatCode) {
                return artFormat;
            }
        }
        return EArtFormat.UNKNOWN_FORMAT;
    }
}
