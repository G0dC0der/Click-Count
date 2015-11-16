package com.pmoradi.security;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public final class Captcha {
    private static final Random RANDOM = new Random();
    private static final char[] CHARS = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            'a','b','c','d','e','f','g','h','i','j','k',    'm','n','o','p','q','r','s','t','u','v','w','x','y','z',
            '0',    '2','3','4','5','6','7','8','9'};

    private transient final String word;
    private int width;
    private int height;


    public Captcha () {
        word = generateWord();
        width = 200;
        height = 150;
    }

    public BufferedImage create() {
        BufferedImage captchaImg = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = captchaImg.createGraphics();
        g2d.setFont(new Font("Serif", Font.PLAIN, 40));
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);

        int x = 5;
        int y = height / 2;
        for (int i = 0; i < word.length(); i++) {
            x += RANDOM.nextInt(10) + 10;
            int r = RANDOM.nextInt(20) - 15;
            if (r > -5)
                y += r;
            else
                y -= r;

            g2d.drawString(word.charAt(i) + "", x, y);
        }
        return captchaImg;
    }

    public boolean isCorrect (String word) {
        return word.equals(this.word);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private String generateWord() {
        int wordLength = RANDOM.nextInt(7) + 5;
        StringBuilder bu = new StringBuilder(wordLength);

        for (int i = 0; i < wordLength; i++)
            bu.append(CHARS[RANDOM.nextInt(CHARS.length)]);

        return bu.toString();
    }
}
