/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Piece;

import Main.BoardPanel;
import Main.ChessBoard;
import Main.Type;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Jayden Tosul
 */
public class Piece {

    public Type type;
    public BufferedImage image;
    public int x, y;
    public int col, row, prevCol, prevRow;
    public int color;
    public Piece hittingP;
    public boolean moved;  //keeps track of which piece has moved
    public boolean twoStep; //for en passant
    
    //constructor
    public Piece(int color, int col, int row) {

        this.color = color;
        this.col = col;
        this.row = row;

        x = getX(col);
        y = getY(row);

        prevCol = col;
        prevRow = row;
    }

    //getters for the x and y
    public final int getX(int col) {
        return col * ChessBoard.SQUARE_SIZE;    //making sure it aligns in the center
    }
    public final int getY(int row) {
        return row * ChessBoard.SQUARE_SIZE;    //making sure it aligns in the center
    }
    //getters for col and row
    public int getCol(int x) {
        return (x + ChessBoard.HALF_SQUARE_SIZE) / ChessBoard.SQUARE_SIZE;
    }
    public int getRow(int y) {
        return (y + ChessBoard.HALF_SQUARE_SIZE) / ChessBoard.SQUARE_SIZE;
    }
    
    //--------------------- POSITION UPDATES -------------------
    public void updatePosition() {  //update piece to new square
        
        if (type == Type.PAWN) {//detects if a pawn advances 2 sqr
            if (Math.abs(row - prevRow) == 2) {
//                System.err.println("twoStepaction");
                twoStep = true;
            }
        }
        
        x = getX(col);
        y = getY(row);
        prevCol = getCol(x);
        prevRow = getRow(y);
        moved = true;   //if a piece has moved it marks it as true for it has moved once
        
    }
    public void resetPosition() {
        col = prevCol;
        row = prevRow;
        x = getX(col);
        y = getY(row);
        
    }
    public int getIndex() {//getst he index of the piece
        for (int index = 0; index < BoardPanel.simPieces.size(); index++) {
            if (BoardPanel.simPieces.get(index) == this) {
                return index;
            }
        }
        return 0;
    }
    
    //------------------------- MOVEMENT ---------------------
    public boolean canMove(int targetCol, int targetRow) {
        return false;
    }
    
    public boolean isWithinBoard(int targetCol, int targetRow) {
        return targetCol >= 0 && targetRow >= 0 && targetCol <= 7 && targetRow <= 7;
    }
    
    //gets the piece that will be captured
    public Piece getHittingP(int targetCol, int targetRow) {
        for (Piece p : BoardPanel.simPieces) {
            if (p.col == targetCol && p.row == targetRow && p != this) {
                return p;   //gets the captured piece
            }
        }
        
        return null;
    }
    //checks if square is valid
    public boolean isValidSquare(int targetCol, int targetRow) {
        hittingP = getHittingP(targetCol, targetRow);
        
        if (hittingP == null) {
            return true;
        } else {
            if (hittingP.color != this.color) {
                return true;
            } else {
                hittingP = null;
            }
        }
        
        return false;
    }
    //checks if same square
    public boolean isSameSquare(int targetCol, int targetRow) {
        return targetCol == prevCol && targetRow == prevRow;
    }
    //checks if there is a piece in the way | up/down & sideways movement
    public boolean isStraightLineBlock(int targetCol, int targetRow) {
        
        //checks the horizontal squares left
        for (int c = prevCol - 1; c > targetCol; c--) { //minus one for original square
            for (Piece piece : BoardPanel.simPieces) {  //sp we start one sqaure left
                if (piece.col == c && piece.row == targetRow) {
                    hittingP = piece;
                    return true;
                }
            }
        }
        //checks the horizontal squares right
        for (int c = prevCol + 1; c < targetCol; c++) { //minus one for original square
            for (Piece piece : BoardPanel.simPieces) {  //start one square right
                if (piece.col == c && piece.row == targetRow) {
                    hittingP = piece;
                    return true;
                }
            }
        }
        //checks vertical up
        for (int r = prevRow - 1; r > targetRow; r--) { //minus one for original square
            for (Piece piece : BoardPanel.simPieces) {  //one square up
                if (piece.col == targetCol && piece.row == r) {
                    hittingP = piece;
                    return true;
                }
            }
        }
        //checks vertical down
        for (int r = prevRow + 1; r < targetRow; r++) { //minus one for original square;
            for (Piece piece : BoardPanel.simPieces) {  //one square down
                if (piece.col == targetCol && piece.row == r) {
                    hittingP = piece;
                    return true;
                }
            }
        }
        
        return false;
    }
    //checks if a piece is blocking the diagonal
    public boolean isDiagonalLineBlock(int targetCol, int targetRow) {
        
        if (targetRow < prevRow) {
            //up left
            for (int c = prevCol - 1; c > targetCol; c--) {
                int diff = Math.abs(c - prevCol);
                for (Piece piece : BoardPanel.simPieces) {
                    if (piece.col == c && piece.row == prevRow - diff) {
                        hittingP = piece;
                        return true;
                    }
                }
            }
            //up right
            for (int c = prevCol + 1; c < targetCol; c++) {
                int diff = Math.abs(c - prevCol);
                for (Piece piece : BoardPanel.simPieces) {
                    if (piece.col == c && piece.row == prevRow - diff) {
                        hittingP = piece;
                        return true;
                    }
                }
            }
        }
        
        if (targetRow > prevRow) {
            //down left
            for (int c = prevCol - 1; c > targetCol; c--) {
                int diff = Math.abs(c - prevCol);
                for (Piece piece : BoardPanel.simPieces) {
                    if (piece.col == c && piece.row == prevRow + diff) {
                        hittingP = piece;
                        return true;
                    }
                }
            }
            //down right
            for (int c = prevCol + 1; c < targetCol; c++) {
                int diff = Math.abs(c - prevCol);
                for (Piece piece : BoardPanel.simPieces) {
                    if (piece.col == c && piece.row == prevRow + diff) {
                        hittingP = piece;
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    //used for castling left side, checking the second square frm left
    public Piece getPieceAt(int col, int row) {
        for (Piece piece : BoardPanel.simPieces) {
            if (piece.col == col && piece.row == row) {
                return piece;
            } 
        }
        
        return null;    //if no piece there
    }

//    --------- This somehow causes some bugs to appear affecting other pieces ---------
//    public boolean isQueenPathClear(int targetCol, int targetRow) {
//        return isStraightLineClear(targetCol, targetRow) &&  //checks rook movement
//                isDiagonalLineClear(targetCol, targetRow);   //checks bishop movement
//    }

    //------------------- IMAGES ---------------------------------
    public final BufferedImage getImage(String filePath) { //gets the images
//        BufferedImage image = null;
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream(filePath + ".png"));
        } catch (IOException e) {
            System.err.println("IMAGE NOT FOUND: " + e.getMessage());
        }
        
        return image;
    }
    //draws the image on the board
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, ChessBoard.SQUARE_SIZE, ChessBoard.SQUARE_SIZE, null);
    }
}
