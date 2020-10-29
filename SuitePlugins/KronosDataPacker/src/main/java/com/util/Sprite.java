package com.util;

import lombok.RequiredArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author ReverendDread on 7/8/2020
 * https://www.rune-server.ee/members/reverenddread/
 * @project ValkyrCacheSuite
 */
@RequiredArgsConstructor
public class Sprite {

    private final int[] palette;
    private final int width, height;

    public Sprite(int width, int height) {
        this(new int[width * height], width, height);
    }

    public static final Sprite createSprite(byte[] data) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(data));
            int width = image.getWidth();
            int height = image.getHeight();
            int[] palette = new int[width * height];
            PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height, palette, 0, width);
            grabber.grabPixels();
            return new Sprite(palette, width, height);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return new Sprite(0, 0);
    }

}
