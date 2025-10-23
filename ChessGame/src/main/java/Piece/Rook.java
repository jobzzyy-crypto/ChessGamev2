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
public class Rook extends Piece {
    
    public Rook(int color, int col, int row) {
        super(color, col, row);
        
        type = Type.ROOK;
        
        if (color == BoardPanel.WHITE) {
           image = getImage("/w_rook");
        } else {
            image = getImage("/b_rook");
        }
    }
    
    //Rook Movement
    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            //rook moves straight up and down / perpendicular
            if (targetCol == prevCol || targetRow == prevRow)  {
                
                //if valid square & no piece in the way
                if (isValidSquare(targetCol, targetRow) 
                        && !isStraightLineBlock(targetCol, targetRow)
                        ) {
                    return true;
                }
                
            }
            
        }
        
        return false;
    }
    
}
