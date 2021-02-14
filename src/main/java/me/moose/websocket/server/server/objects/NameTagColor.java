package me.moose.websocket.server.server.objects;

import java.awt.*;
import java.security.PublicKey;

public class NameTagColor {
    public static String[] run(int input) {
        return new String[] {String.valueOf(getRed(input)), String.valueOf(getGreen(input)), String.valueOf(getBlue(input))};
    }
    public static void main(String[] args) {
        int input = 255;
        System.out.println(new Color(0x00EBFF));
        System.out.println("R: " + getRed(input));
        System.out.println("G: " + getGreen(input));
        System.out.println("B: " + getBlue(input));
       System.out.println(Color.getHSBColor(getRed(input), getGreen(input), getBlue(input)));

    }

    public static float getRed(int n) {
        return (float) getRedInt(n) / 255.0f;
    }

    public static int getRedInt(int n) {
        return n >> 16 & 255;
    }

    public static float getGreen(int n) {
        return (float) getGreenInt(n) / 255.0f;
    }

    public static int getGreenInt(int n) {
        return n >> 8 & 255;
    }

    public static float getBlue(int n) {
        return (float) getBlueInt(n) / 255.0f;
    }

    public static int getBlueInt(int n) {
        return n & 255;
    }

    public static int reverseBits(int n)
    {
        int rev = 0;

        // traversing bits of 'n'
        // from the right
        while (n > 0)
        {
            // bitwise left shift
            // 'rev' by 1
            rev <<= 1;

            // if current bit is '1'
            if ((int)(n & 1) == 1)
                rev ^= 1;

            // bitwise right shift
            //'n' by 1
            n >>= 1;
        }
        // required number
        return rev;
    }

    //a





    public static float lIIlIllIIlllIlllIIlllIIIl(int n) {
        return (float)llIllllIIIIlllIIIIIIlIIll(n) / 255.0f;
    }

    public static int llIllllIIIIlllIIIIIIlIIll(int n) {
        return n >> 24 & 0xFF;
    }

    public static int IIlIIIlIIIIllIIIllllIlllI(int n, int n2, int n3, int n4) {
        return (n4 & 0xFF) << 24 | (n & 0xFF) << 16 | (n2 & 0xFF) << 8 | n3 & 0xFF;
    }

    public static int IIlIIIlIIIIllIIIllllIlllI(float f, float f2, float f3, float f4) {
        return IIlIIIlIIIIllIIIllllIlllI((int)((double)(f * 255.0f) + 0.5), (int)((double)(f2 * 255.0f) + 0.5), (int)((double)(f3 * 255.0f) + 0.5), (int)((double)(f4 * 255.0f) + 0.5));
    }

    public static int IIlIIIlIIIIllIIIllllIlllI(int n, float f) {
        int n2 = (int)((float)llIllllIIIIlllIIIIIIlIIll(n) * f);
        return n2 << 24 | n & 0xFFFFFF;
    }
}
