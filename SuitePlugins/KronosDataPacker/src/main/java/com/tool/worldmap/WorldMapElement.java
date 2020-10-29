package com.tool.worldmap;

import com.tool.worldmap.alignment.HorizontalAlignment;
import com.tool.worldmap.alignment.VerticalAlignment;
import com.util.Misc;
import store.io.impl.InputStream;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
public class WorldMapElement {

    private int sprite1, sprite2;
    public int textSize;
    private String[] menuActions;
    private int field3194, field3192, field3196, field3197, field3189;
    private int[] field3183, field3188, field3201;
    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;
    private int category;
    private final int objectId;
    public String name;
    private String menuTargetName;

    public WorldMapElement(int objectId) {
        this.sprite1 = -1;
        this.sprite2 = -1;
        this.textSize = 0;
        this.menuActions = new String[5];
        this.field3194 = Integer.MAX_VALUE;
        this.field3192 = Integer.MAX_VALUE;
        this.field3196 = Integer.MIN_VALUE;
        this.field3197 = Integer.MIN_VALUE;
        this.horizontalAlignment = HorizontalAlignment.CENTER;
        this.verticalAlignment = VerticalAlignment.CENTER;
        this.category = -1;
        this.objectId = objectId;
    }

    public void readValues(InputStream buffer) {
        while (true) {
            int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                return;
            }
            decode(buffer, opcode);
        }
    }

    void decode(InputStream buffer, int opcode) {
        if(opcode == 1) {
            this.sprite1 = buffer.readBigSmart();
        } else if(opcode == 2) {
            this.sprite2 = buffer.readBigSmart();
        } else if(opcode == 3) {
            this.name = buffer.readString();
        } else if(opcode == 4) {
            this.field3189 = buffer.read24BitInt();
        } else if(opcode == 5) {
            buffer.read24BitInt();
        } else if(opcode == 6) {
            this.textSize = buffer.readUnsignedByte();
        } else {
            int var3;
            if(opcode == 7) {
                buffer.readUnsignedByte();
            } else if(opcode == 8) {
                buffer.readUnsignedByte();
            } else if(opcode >= 10 && opcode <= 14) {
                this.menuActions[opcode - 10] = buffer.readString();
            } else if(opcode == 15) {
                var3 = buffer.readUnsignedByte();
                this.field3183 = new int[var3 * 2];

                int var4;
                for(var4 = 0; var4 < var3 * 2; ++var4) {
                    this.field3183[var4] = buffer.readShortLE();
                }

                buffer.readInt();
                var4 = buffer.readUnsignedByte();
                this.field3188 = new int[var4];

                int var5;
                for(var5 = 0; var5 < this.field3188.length; ++var5) {
                    this.field3188[var5] = buffer.readInt();
                }

                this.field3201 = new int[var3];

                for(var5 = 0; var5 < var3; ++var5) {
                    this.field3201[var5] = buffer.readByte();
                }
            } else if(opcode != 16) {
                if(opcode == 17) {
                    this.menuTargetName = buffer.readString();
                } else if(opcode == 18) {
                    buffer.readBigSmart();
                } else if(opcode == 19) {
                    this.category = buffer.readUnsignedShort();
                } else if(opcode == 21) {
                    buffer.readInt();
                } else if(opcode == 22) {
                    buffer.readInt();
                } else if(opcode == 23) {
                    buffer.readUnsignedByte();
                    buffer.readUnsignedByte();
                    buffer.readUnsignedByte();
                } else if(opcode == 24) {
                    buffer.readShortLE();
                    buffer.readShortLE();
                } else if(opcode == 25) {
                    buffer.readBigSmart();
                } else if(opcode == 28) {
                    buffer.readUnsignedByte();
                } else if(opcode == 29) {
                    this.horizontalAlignment = (HorizontalAlignment) Misc.getEnumeratedTypeIndex(HorizontalAlignment.alignments(), buffer.readUnsignedByte());
                } else if(opcode == 30) {
                    this.verticalAlignment = (VerticalAlignment) Misc.getEnumeratedTypeIndex(VerticalAlignment.alignments(), buffer.readUnsignedByte());
                }
            }
        }

    }

}
