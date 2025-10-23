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
public class King extends Piece {
    
    public King(int color, int col, int row) {
        super(color, col, row);
        
        type = Type.KING;
        
        if (color == BoardPanel.WHITE) {
           image = getImage("/w_king");
        } else {
            image = getImage("/b_king");
        }
        
    }

    //King movement
    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow)) {
            //kings moves one square in all direction
            if (Math.abs(targetCol - prevCol) + Math.abs(targetRow - prevRow) == 1 ||   //up & down / left & right movement
                    Math.abs(targetCol - prevCol) * Math.abs(targetRow - prevRow) == 1) {//diagnol movement
                
                if (isValidSquare(targetCol, targetRow)) {
                    return true;
                }
            }
            
            //caslting
            if (moved == false) {
                //right calting
                if (targetCol == prevCol + 2 && targetRow == prevRow
                        && !isStraightLineBlock(targetCol, targetRow)   //this only checks square next to king
                        && getHittingP(targetCol, targetRow) == null) { //checks 2nd square next to king
                    //checks if the rook hasnt moved
                    for(Piece piece : BoardPanel.simPieces) {
                        if (piece.col == prevCol + 3 && piece.row == prevRow
                                && !piece.moved) {
//                            System.out.println("right Castling");
                            BoardPanel.castlingP = piece;
                            return true;
                        }
                    }
                }
                //left castling
                if (targetCol == prevCol - 2 && targetRow == prevRow//checks 2 sqaures left if empty
                        && !isStraightLineBlock(targetCol, targetRow)
                        && getHittingP(targetCol, targetRow) == null) {
                    //create array to store piece to check if no pieces in 3 square
                    Piece p[] = new Piece[2];
                    for (Piece piece : BoardPanel.simPieces) {
                        p[0] = getPieceAt(1, targetRow);//gets the piece at -3 squares from king left side
                        
                        if (piece.col == prevCol - 4 && piece.row == targetRow) {
//                            System.err.println("yes2");
                            p[1] = piece;
                        }
                        
                        if (p[0] == null && p[1] != null && !p[1].moved) {
//                            System.out.println("Left Castling");
                            BoardPanel.castlingP = p[1];
                            return true;
                        }
                        
                    }
                    
                }
                
            }
            
        }
        
        return false;
    }
    
}
