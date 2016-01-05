package com.pmoradi.security;

import com.pmoradi.system.SystemSetup;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public final class Captcha {
    private static final Random RANDOM = new Random();
    private static final char[] CHARS = {
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            'a','b','c','d','e','f','g','h','i','j','k',    'm','n','o','p','q','r','s','t','u','v','w','x','y','z',
            '0',    '2','3','4','5','6','7','8','9'};

    private transient final String word;
    private int width;
    private int height;
    private int minChars;
    private int maxChars;
    private long expiringTime;

    public Captcha () {
        this(200, 150, 6, 6, null, System.currentTimeMillis() + 60_000);
    }

    public Captcha (int width, int height, int minChars, int maxChars, String word, long expiringTime) {
        this.width = width;
        this.height = height;
        this.minChars = minChars;
        this.maxChars = maxChars;
        this.word = word != null ? word : generateWord();
        this.expiringTime = expiringTime;
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
            x += RANDOM.nextInt(10) + 7;
            int r = RANDOM.nextInt(20) - 15;
            if (r > -5)
                y += r;
            else
                y -= r;

            g2d.drawString(word.charAt(i) + "", x, y);
        }
        return captchaImg;
    }

    public boolean hasExpired(){
        return expiringTime > System.currentTimeMillis();
    }

    public boolean isCorrect (String word) {
        return !hasExpired() && word.equals(this.word);
    }

    private String generateWord() {
        int wordLength = minChars == maxChars ? minChars : RANDOM.nextInt(maxChars - minChars) + minChars;
        StringBuilder bu = new StringBuilder(wordLength);

        for (int i = 0; i < wordLength; i++)
            bu.append(CHARS[RANDOM.nextInt(CHARS.length)]);

        return bu.toString();
    }
}
