/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Piece;

import Main.BoardPanel;
import Main.Type;

/**
 *
 * @author Jayden Tosul
 */
public class Queen extends Piece {
    
    public Queen(int color, int col, int row) {
        super(color, col, row);
        
        type = Type.QUEEN;
        
        if (color == BoardPanel.WHITE) {
           image = getImage("/w_queen");
        } else {
            image = getImage("/b_queen");
        }
    }
    
    //Queen Movements
    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            //queen moves to any squares sideways and diagonally and up/down
            //had to separate due to some bugs appear when using isQueenPathClear()
            //rook movement/perpendicular
            if (targetCol == prevCol || targetRow == prevRow) {    
                if (isValidSquare(targetCol, targetRow) && 
                        !isStraightLineBlock(targetCol, targetRow)) {
//                    System.err.println("Perpendicular movement");
                    return true;
                }
            }
            //bishop movement/diagonal
            if (Math.abs(targetCol - prevCol) == Math.abs(targetRow - prevRow)) {
                if (isValidSquare(targetCol, targetRow) && 
                        !isDiagonalLineBlock(targetCol, targetRow)) {
//                    System.err.println("Diagonal movement");
                    return true;
                }

            }

        }
        
        return false;
    }
    
}
