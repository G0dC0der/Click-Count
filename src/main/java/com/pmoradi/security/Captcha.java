package com.pmoradi.security;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public final class Captcha
{
    private static final int IMG_WIDTH  = 200;
    private static final int IMG_HEIGHT = 150;
    private static final char[] CHARS = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            'a','b','c','d','e','f','g','h','i','j','k',    'm','n','o','p','q','r','s','t','u','v','w','x','y','z',
            '0',    '2','3','4','5','6','7','8','9'};

    private transient final String word;
    private transient final Random random;

    public Captcha () {
        random = new Random();
        word = generateWord();
    }

    public BufferedImage create() {
        BufferedImage captchaImg = new BufferedImage(IMG_WIDTH,IMG_HEIGHT,BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = captchaImg.createGraphics();
        g2d.setFont(new Font("Serif", Font.PLAIN, 40));
        g2d.setPaint(Color.WHITE);
        g2d.fillRect (0, 0, IMG_WIDTH, IMG_HEIGHT);
        g2d.setColor(Color.BLACK);

        int x = 5, y = IMG_HEIGHT / 2;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            g2d.drawString(c + "", x, y);
            x += random.nextInt(10) + 10;
            int r = random.nextInt(20) - 15;
            if (random.nextInt(10) > 5)
                y += r;
            else
                y -= r;
        }

        return captchaImg;
    }

    public boolean isCorrect (String word) {
        return word.equals(this.word);
    }

    private String generateWord() {
        int wordLength = random.nextInt(7) + 5;
        StringBuilder bu = new StringBuilder(wordLength);

        for (int i = 0; i < wordLength; i++)
            bu.append(CHARS[random.nextInt(CHARS.length)]);

        return bu.toString();
    }
}
