/*
 * Primitives.java
 *
 * Created on 14 de febrero de 2005, 21:22
 */

package org.jtgl.core;

/**
 * This class is a place holder for some primitives drawing code.
 * @author Manuel Polo (manuel_polo@yahoo.es)
 */
public final class Primitives {
    
    
    /**
     * Fills a defined triangle with current foreground color.
     * Author: Børge Nygaard <baustvold@hotmail.com>
     *
     * @param gfx JTGLGraphics drawing context
     * @param wx1 X coordinate of triangle vertex 1
     * @param wy1 Y coordinate of triangle vertex 1
     * @param wx2 X coordinate of triangle vertex 2
     * @param wy2 Y coordinate of triangle vertex 2
     * @param wx3 X coordinate of triangle vertex 3
     * @param wy3 Y coordinate of triangle vertex 3
     */
    public static void fillDirectTriangle(JTGLGraphics gfx,int wx1, int wy1, int wx2, int wy2, int wx3, int wy3) { 
        // First of all we sort the triangles based on the
        // y value y1 at top, y2 in the middle and y3 at the end
        int x1 = wx1, y1 = wy1, x2 = wx2, y2 = wy2, x3 = wx3, y3 = wy3;
        if (wy1 > wy2) {
            if (wy1 > wy3) {
                y3 = wy1;
                x3 = wx1;
                if (wy2 > wy3) {
                    y2 = wy2;
                    x2 = wx2;
                    y1 = wy3;
                    x1 = wx3;
                } else {
                    y1 = wy2;
                    x1 = wx2;
                    y2 = wy3;
                    x2 = wx3;
                }
            } else {
                x2 = wx1;
                y2 = wy1;
                if (wy2 > wy3) {
                    y3 = wy2;
                    x3 = wx2;
                    y1 = wy3;
                    x1 = wx3;
                } else {
                    y1 = wy2;
                    x1 = wx2;
                    x3 = wx3;
                    y3 = wy3;
                }
            }
        } else if (wy1 > wy3) {
            x2 = wx1;
            y2 = wy1;
            if (wy2 > wy3) {
                x1 = wx3;
                y1 = wy3;
                x3 = wx2;
                y3 = wy2;
            } else {
                x1 = wx2;
                y1 = wy2;
                x3 = wx3;
                y3 = wy3;
            }
        } else {
            x1 = wx1;
            y2 = wy1;
            if (wy2 > wy3) {
                x2 = wx3;
                y2 = wy3;
                x3 = wx2;
                y3 = wy2;
            } else {
                x2 = wx2;
                y2 = wy2;
                x3 = wx3;
                y3 = wy3;
            }
        }
        boolean y3by2 = y3 >= y2;
        boolean y2by1 = y2 >= y1;
        boolean y3by1 = y3 > y1;
        int sx = x1;
        int scanline = Math.max(y1, 0);
        int ex = x1;
        int x3mx1 = x3 - x1;
        int y3my1 = y3 - y1;
        int x2mx1 = x2 - x1;
        int y2my1 = y2 - y1;
        int x2mx2 = x3 - x2;
        int y2my2 = y3 - y2;
        int maxLineBottom = y3;
        int maxLineHalf = y2;
        for (; scanline < maxLineHalf; scanline++) {
            if (y3by2) {
                // TODO: The amount added should follow a more advanced line draw algorithm
                sx = x1 + x3mx1 * (scanline - y1) / y3my1;
            }
            if (y2by1) {
                // TODO: The amount added should follow a more advanced line draw algorithm
                ex = x1 + x2mx1 * (scanline - y1) / y2my1;
            }
            gfx.drawLine(sx, scanline, ex, scanline);
        }
        ex = x2;
        for (; scanline < maxLineBottom; scanline++) {
            if (y3by1) {
                // TODO: The amount added should follow a more advanced line draw algorithm
                sx = x1 + x3mx1 * (scanline - y1) / y3my1;
            }
            if (y3by2) {
                // TODO: The amount added should follow a more advanced line draw algorithm
                ex = x2 + x2mx2 * (scanline - y2) / y2my2;
            }
            gfx.drawLine(sx, scanline, ex, scanline);
        }
    }    
}
