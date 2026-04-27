package com.miujoke.fundadmin.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtils {

    private static final char[] CODE_CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z'
    };

    private static final Random RANDOM = new Random();

    public static CaptchaResult generateCaptcha(int width, int height, int codeLength) {
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            codeBuilder.append(CODE_CHARS[RANDOM.nextInt(CODE_CHARS.length)]);
        }
        String code = codeBuilder.toString();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 背景
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 边框
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, width - 1, height - 1);

        // 干扰线
        for (int i = 0; i < 6; i++) {
            g.setColor(randomColor());
            g.drawLine(RANDOM.nextInt(width), RANDOM.nextInt(height),
                       RANDOM.nextInt(width), RANDOM.nextInt(height));
        }

        // 干扰点
        for (int i = 0; i < 30; i++) {
            g.setColor(randomColor());
            g.fillRect(RANDOM.nextInt(width), RANDOM.nextInt(height), 2, 2);
        }

        // 验证码文字
        g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 28));
        int x = (width - codeLength * 22) / 2;
        for (int i = 0; i < codeLength; i++) {
            g.setColor(randomColor());
            g.drawString(String.valueOf(code.charAt(i)), x + i * 22, 30);
        }

        g.dispose();
        return new CaptchaResult(code, image);
    }

    private static Color randomColor() {
        return new Color(RANDOM.nextInt(150), RANDOM.nextInt(150), RANDOM.nextInt(150));
    }

    public record CaptchaResult(String code, BufferedImage image) {}
}