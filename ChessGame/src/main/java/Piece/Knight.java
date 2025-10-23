/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Piece;

import Main.BoardPanel;
import Main.Type;

/**
 *
 * @author User
 */
public class Knight extends Piece {
    
    public Knight(int color, int col, int row) {
        super(color, col, row);
        
        type = Type.KNIGHT;
        
        if (color == BoardPanel.WHITE) {
           image = getImage("/w_knight");
        } else {
            image = getImage("/b_knight");
        }
        
    }
    
    //Knight Movement
    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow)) {
            //knight moves either 2:1 or 1:2
            if (Math.abs(targetCol - prevCol) * Math.abs(targetRow - prevRow) == 2) {
                //check if the sqaure is valid
                if (isValidSquare(targetCol, targetRow)) {
                    return true;
                }
                
            }
            
        }
        
        return false;
    }
    
}
