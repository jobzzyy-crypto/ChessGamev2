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
public class Pawn extends Piece {
    
    public Pawn(int color, int col, int row) {
        super(color, col, row);
        
        type = Type.PAWN;
        
        if (color == BoardPanel.WHITE) {
            image = getImage("/w_pawn");
        } else {
            image = getImage("/b_pawn");
        }
    }
    
    //Pawn Movement
    @Override
    public boolean canMove(int targetCol, int targetRow) {
        //pawn moves forward either 1/2 squares, captures diagonally and cant move if a piece in front
        if(isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            
            int moveV;  //move value determining the whos turn
            if (color == BoardPanel.WHITE) {
                moveV = -1; //up
            } else {
                moveV = 1;
            }
            
            hittingP = getHittingP(targetCol, targetRow);//gets the hitiing piece for checking
            //1 square movement
            if (targetCol == prevCol 
                    && targetRow == prevRow + moveV 
                    && hittingP == null) {
//                System.out.println("1 sqaure");
                return true;
            }
            //2 square movement
            if (targetCol == prevCol 
                    && targetRow == prevRow + moveV * 2 
                    && hittingP == null
                    && !moved 
                    && !isStraightLineBlock(targetCol, targetRow)) {
//                System.out.println("2 sqaure");
                return true;
            }
            
            //diagonal capture
            if (Math.abs(targetCol - prevCol) == 1 
                    && targetRow == prevRow + moveV 
                    && hittingP != null
                    && hittingP.color != this.color) {
//                System.out.println("diagonal capture");
                return true;
            }
        
            //En Passant
            if (Math.abs(targetCol - prevCol) == 1 && targetRow == prevRow + moveV) {
//                System.out.println("enpassanting");
                for (Piece piece : BoardPanel.simPieces) {
                    if (piece.col == targetCol && piece.row == prevRow && piece.twoStep) {
//                        System.out.println("capture w enpassant");
                        hittingP = piece;
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
}
