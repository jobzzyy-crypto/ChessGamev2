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
public class Bishop extends Piece {
    
    public Bishop(int color, int col, int row) {
        super(color, col, row);
        
        type = Type.BISHOP;
        
        if (color == BoardPanel.WHITE) {
           image = getImage("/w_bishop");
        } else {
            image = getImage("/b_bishop");
        }
    }
    
    //Bishop Movement
    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            //bishop movement (diagonal) 1:1
            if (Math.abs(targetCol - prevCol) == Math.abs(targetRow - prevRow)) {
                
                if (isValidSquare(targetCol, targetRow) && 
                        !isDiagonalLineBlock(targetCol, targetRow)) {
                    return true;
                }
                
            }
            
        }
        
        return false;
    }
}
