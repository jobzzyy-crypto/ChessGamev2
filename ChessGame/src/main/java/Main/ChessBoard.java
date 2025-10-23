/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Jayden Tosul
 */
public class ChessBoard {
    
    final int MAX_COL = 8;
    final int MAX_ROW = 8;
    public static final int SQUARE_SIZE = 80;
    public static final int HALF_SQUARE_SIZE = SQUARE_SIZE / 2; //this is for aligning in the center
    
    public void draw(Graphics2D g2) {
        int c = 1;  //alternates the color
        
        //paints the row
        for (int row = 0; row < MAX_ROW; row++) {
            //each columns
            for (int col = 0; col < MAX_COL; col++) {
                
                //alternate between the colors
                if (c == 1) {
                    g2.setColor(new Color(161, 214, 166));
                    c = 0;
                } else {
                    g2.setColor(new Color(31, 115, 39));
                    c = 1;
                }
                //paints the squares
                g2.fillRect(col * SQUARE_SIZE , row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
            //keep alternating
            if (c == 0) {
                c = 1;
            } else {
                c = 0;
            }
        }
        
    }
    
}
