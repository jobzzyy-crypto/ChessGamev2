/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Piece.Bishop;
import Piece.King;
import Piece.Knight;
import Piece.Pawn;
import Piece.Piece;
import Piece.Queen;
import Piece.Rook;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author User
 */
public class BoardPanel extends JPanel implements Runnable {
    
    private final int panelWidth = 640 + 200;
    private final int panelHeight = 640;
    
    //gamethread / speed / mouse
    final int FPS = 60;
    Thread gameThread;
    ChessBoard board = new ChessBoard();
    Mouse mouse = new Mouse();
    
    //color
    public static final int WHITE = 1;
    public static final int BLACK = 0;
    int currentColor = WHITE;
    public int getCurrentColor() { return this.currentColor; }
    
    //BOOLEANS
    boolean canMove;
    boolean isValidSquare;
    boolean promotion;  //handeling promotion
    boolean gameOver;
    boolean stalemate;

    
    //list for storing the pieces
    public static ArrayList<Piece> capturedP = new ArrayList<>();
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();   //backup list
    public static ArrayList<Piece> promoPieces = new ArrayList<>();
    Piece activeP, checkingP;  //stores the piece that the player clicks
    public static Piece castlingP;
    
    //CONSTRUCTOR
    public BoardPanel() {
        this.setFocusable(true);
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setBackground(new Color(158, 83, 38));
        addMouseListener(mouse);    //mouse functionality
        addMouseMotionListener(mouse);
        
        //places the chess pieces
        setPieces();
//        testing();
//        checkMate();
        copyPieces(pieces, simPieces);
        
    }
    
    //adds the pieces to the list
    public final void setPieces() {
        //------- White Pieces -------
        pieces.add(new Pawn(WHITE, 0, 6));
        pieces.add(new Pawn(WHITE, 1, 6));
        pieces.add(new Pawn(WHITE, 2, 6));
        pieces.add(new Pawn(WHITE, 3, 6));
        pieces.add(new Pawn(WHITE, 4, 6));
        pieces.add(new Pawn(WHITE, 5, 6));
        pieces.add(new Pawn(WHITE, 6, 6));
        pieces.add(new Pawn(WHITE, 7, 6));
        pieces.add(new Rook(WHITE, 0, 7));
        pieces.add(new Knight(WHITE, 1, 7));
        pieces.add(new Bishop(WHITE, 2, 7));
        pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new King(WHITE, 4, 7));
        pieces.add(new Bishop(WHITE, 5, 7));
        pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        
        //-------Black Pieces ---------
        pieces.add(new Pawn(BLACK, 0, 1));
        pieces.add(new Pawn(BLACK, 1, 1));
        pieces.add(new Pawn(BLACK, 2, 1));
        pieces.add(new Pawn(BLACK, 3, 1));
        pieces.add(new Pawn(BLACK, 4, 1));
        pieces.add(new Pawn(BLACK, 5, 1));
        pieces.add(new Pawn(BLACK, 6, 1));
        pieces.add(new Pawn(BLACK, 7, 1));
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Knight(BLACK, 1, 0));
        pieces.add(new Bishop(BLACK, 2, 0));
        pieces.add(new Queen(BLACK, 3, 0));
        pieces.add(new King(BLACK, 4, 0));
        pieces.add(new Bishop(BLACK, 5, 0));
        pieces.add(new Knight(BLACK, 6, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        
    }
    //---------------- TESTING ---------------
    private void testing() {
        pieces.add(new Pawn(BLACK, 3, 1));
        pieces.add(new Pawn(WHITE, 2, 6));
        pieces.add(new Bishop(BLACK, 2, 0));
        pieces.add(new King(WHITE, 4, 7));
        pieces.add(new King(BLACK, 4, 0));
        pieces.add(new Rook(WHITE, 7, 7));
        pieces.add(new Queen(BLACK, 3, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new Bishop(WHITE, 5, 7));
    }
    private void checkMate() {
        pieces.add(new King(WHITE, 4, 7));
        pieces.add(new King(BLACK, 4, 0));
        pieces.add(new Queen(BLACK, 2, 6));
        pieces.add(new Rook(BLACK, 7, 6));
        pieces.add(new Pawn(WHITE, 5, 6));
        pieces.add(new Queen(WHITE, 3, 7));
        
    }
    
    
    //this just copies the pieces to another list every game for back up purposes
    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
        target.clear();
        
        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
    }
    //launches the game
    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    @Override
    public void run() {
        
        /**
         * GAMELOOP
         * found this on a YouTube video explaining how a game loop works
         * that would utilize update, thread, draw. Basically, it makes the
         * game run in 60fps
         */
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        
        while(gameThread != null) {
            currentTime = System.nanoTime();
            
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
        
    }
    
    //updates the game
    public void update() {

        if (promotion) {
            promoting();

        } else if (!gameOver && !stalemate) {

            //detects when the player clicks
            if (mouse.pressed) {
                if (activeP == null) {
                    //if activeP is null it goes thru the pieces
                    for (Piece piece : simPieces) {
                        //if white/blacks turn then it picks up their piece
                        if (piece.color == currentColor
                                && piece.col == mouse.x / ChessBoard.SQUARE_SIZE
                                && piece.row == mouse.y / ChessBoard.SQUARE_SIZE) {
                            activeP = piece;
                        }
                    }
                } else {    //if player is holding a piece
                    simulate(); //call simulate
                }

            }
            //player releases a piece
            if (mouse.pressed == false) {
                if (activeP != null) {

                    if (isValidSquare) {//update position if square is valid
                        //confirm action
                        
                        copyPieces(simPieces, pieces);
                        activeP.updatePosition();

                        //update castling
                        if (castlingP != null) {    //castles the king and rook
                            castlingP.updatePosition();
                        }
                        
                        if (isKingInCheck() && isCheckmate()) { //gameOver
                            System.err.println("gameove");
                            gameOver = true;
                            
                        } else if (isStalemate() && !isKingInCheck()) {
                            stalemate = true;
                            
                        } else { //continue

                            if (canPromote()) {
                                promotion = true;
                            } else {
                                changePlayerT();    //change player
                            }
                            
                        }
                        
                    } else {
                        activeP.resetPosition();
                        activeP = null;
                        
                    }

//                    activeP = null;   //remove so it will not throw exception when promoting

                }

            }

        }

    }
    
    private void simulate() {    //simulates the players movement
        if (activeP == null) return;
        
        canMove = false;
        isValidSquare = false;
        
        //resets the pieces back after evry move
        copyPieces(pieces, simPieces);//restoring the piece incase player cancels a move
        
        //reset rook after loop
        if(castlingP != null)  {
            castlingP.col = castlingP.prevCol;
            castlingP.x = castlingP.getX(castlingP.col);
            castlingP = null;
            
        }
        
        //updates the mouse when player holds a piece
        activeP.x = mouse.x - ChessBoard.HALF_SQUARE_SIZE;
        activeP.y = mouse.y - ChessBoard.HALF_SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);
        
        //checks the movement of each piece
        if (activeP.canMove(activeP.col, activeP.row)) {
            canMove = true;
            
            //if can capture a piece, remove it and add to captured list
            if (activeP.hittingP !=  null) {
//                capturedP.add(simPieces.remove(activeP.hittingP.getIndex()));
                simPieces.remove(activeP.hittingP.getIndex());
            }
            
            checkCastling();
            
            if (!isIllegal(activeP) && !opponentCanCaptureKing()) {  //makes sure king doesnt walk into check
                isValidSquare = true;
            }
        }
        
    }
    
    //------------- CHECKING FOR ILLEGAL MOVES ----------
    private boolean isIllegal(Piece king) {
        
        if (king.type == Type.KING) {
            for (Piece piece : simPieces) {
                //checks the square the king is walking into
                if (piece != king && piece.color != king.color && piece.canMove(king.col, king.row)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    private boolean isKingInCheck() {
        
        Piece king = getKing(true); //true for opponent king & false for your king
        
        if (activeP.canMove(king.col, king.row)) {
            System.err.println("\n-------- King is in Check ----------\n");
            checkingP = activeP;
            System.err.println("Checking Piece: " + (checkingP.color == WHITE ? "WHITE" : "BLACK"));
            System.err.println("isKingInCheck: TRUE");
            return true;
        }
        
        return false;
    }
    private Piece getKing(boolean opponent) {
        
        Piece king = null;
        
        for(Piece piece : simPieces) {
            if(opponent) {
                if (piece.type == Type.KING && piece.color != currentColor) {
                    king = piece;
                }
            } else {
                if (piece.type == Type.KING && piece.color == currentColor) {
                    king = piece;
                }
            }
        }
        
        return king;
    }
    private Piece getKingByColor(int color) {   //gets king by color
        for (Piece piece : simPieces) {
            if (piece instanceof King && piece.color == color) {
                return piece;
            }
        }
        return null;
    }
    private boolean opponentCanCaptureKing() {
        Piece king = getKing(false);    //false to get your king
        
        if (king.type == Type.KING) {
            for (Piece piece : simPieces) {
                //checks if the king is walking into a check
                if (piece != king && piece.color != king.color && piece.canMove(king.col, king.row)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    //-------------- CHECKMATE------------------
    private boolean isCheckmate() {
        System.err.println("\n---------- Checking for CheckMate -------\n");
        
        if (checkingP == null) return false;
        
        int attackerColor = checkingP.color;
        int defenderColor = (attackerColor == WHITE) ? BLACK : WHITE;
        
        Piece defKing = getKingByColor(defenderColor);  //gets the defending king
        System.err.println("Defending King: " + (defKing.color == WHITE ? "WHITE" : "BLACK"));
        if (kingCanMove(defKing)) {    //if king stilll has legal moves
            System.err.println("King can move");
            return false;
        }
        
//        //check if there are multiple pieces attacking
//        int attackers = 0;
//        for (Piece piece : simPieces) {
//            if (piece.color != currentColor) {  //make sure its not the same color
//                if (piece.canMove(king.col, king.row)) attackers++;
//                if (attackers >= 2) return true;
//            }
//        }
        
        //check if a defender can capture the piece
        for (Piece piece : simPieces) {
            if (piece.color == defenderColor && piece != defKing) {
                if (piece.canMove(checkingP.col, checkingP.row)) {
                    System.err.println("Defender can Capture!!");
                    return false;
                }
            }
        }
        
        //if the checking piece a slider, we can block
        boolean isSlider = 
                (checkingP instanceof Queen) ||
                (checkingP instanceof Rook) ||
                (checkingP instanceof Bishop);
        
        
        if (isSlider) {
            int dc = Integer.signum(defKing.col - checkingP.col);
            int dr = Integer.signum(defKing.row - checkingP.row);
            
            int c = checkingP.col + dc; //so it does not start on the checkers square
            int r = checkingP.row + dr;
            // iterate squares strictly between checker and king
            while (c != defKing.col || r != defKing.row) {
                for (Piece piece : simPieces) {
                    if (piece != defKing && piece.color == defenderColor) {         // any defender except the king
                        if (piece.canMove(c, r)) {
                            System.err.println("Defender can Block!!");
                            return false;                                       // a block exists → not mate
                        }
                    }
                }
                c += dc;
                r += dr;
            }

        }


        //king cant move, checkmate
        System.err.println("NO Legal Moves or Captures");
        System.err.println("CHECKMATE");
        return true;
    }
    private boolean kingCanMove(Piece king) {
        System.err.println("Checking: kingCanMove()");
        
        //checks all the king moves
        if (isKingValidMove(king, -1, -1)) {return true;}
        if (isKingValidMove(king, 0, -1)) {return true;}
        if (isKingValidMove(king, 1, -1)) {return true;}
        if (isKingValidMove(king, 1, 0)) {return true;}
        if (isKingValidMove(king, 1, 1)) {return true;}
        if (isKingValidMove(king, 0, 1)) {return true;}
        if (isKingValidMove(king, -1, 1)) {return true;}
        if (isKingValidMove(king, -1, 0)) {return true;}
        
        System.err.println("King Cannot Move");
        return false;
    }
    private boolean isKingValidMove(Piece king, int colPlus, int rowPlus) {
        boolean isValidMove = false;
        
        //update king position
        king.col += colPlus;
        king.row += rowPlus;
        
        if (king.canMove(king.col, king.row)) {
            if (king.hittingP != null) {
                simPieces.remove(king.hittingP.getIndex());
            }
            if (isIllegal(king) == false) {
                isValidMove = true;
            }
        }
        //reset the kings position
        king.resetPosition();
        copyPieces(pieces, simPieces);
        
        System.err.println("isValideMove(): " + isValidMove);
        return isValidMove;
    }
    
    //------------------- STALEMATE ---------------------
    private boolean isStalemate() {
        
        int count = 0;  //counts the number of pieces left
        for (Piece piece : simPieces) {
            if (piece.color != currentColor) {
                count++;    //if WHITE, then count BLACK pieces and viceversa
            }
        }
        
        //last piece king and if no moves then stalement
        if (count == 1) {
            if (kingCanMove(getKing(true)) == false) {
                return true;
            }
        }
        
        return false;
    }
    
    //------------ CHECKING CASTLING ----------------
    private void checkCastling() {
        
        if (castlingP != null) {//if col is 0 move rook 3 right
            if (castlingP.col == 0) {
                castlingP.col += 3;
            } else if (castlingP.col == 7) {//if col is 7 move rook left
                castlingP.col -= 2;
            }

            castlingP.x = castlingP.getX(castlingP.col);
        }

    }
    
    //------------------CHATGPT -----------------------
    private void recomputeCheckForSideToMove() {
        //get king
        Piece myKing = getKing(false);
        checkingP = null;
        
        if (myKing == null) return;
        
        
        for (Piece piece : simPieces) {
            if (piece.color != currentColor && piece.canMove(myKing.col, myKing.row)) {
                checkingP = piece;
                break;
            }
        }
        
    }
    
    //----------------- PAWN PROMOTION ------------------
    private boolean canPromote() {
        
        if (activeP.type == Type.PAWN) {
            if (currentColor == WHITE && activeP.row == 0 || currentColor == BLACK && activeP.row == 7) {
                promoPieces.clear();
                promoPieces.add(new Queen(currentColor, 9, 5));
                promoPieces.add(new Rook(currentColor, 9, 4));
                promoPieces.add(new Bishop(currentColor, 9, 3));
                promoPieces.add(new Knight(currentColor, 9, 2));
                return true;
            }
        }
        
        return false;
    }
    //promoting a pawn
    private void promoting() {
        
        if (mouse.pressed) {
            for (Piece piece : promoPieces) {
                if (piece.col == mouse.x / ChessBoard.SQUARE_SIZE && piece.row == mouse.y / ChessBoard.SQUARE_SIZE) {
                    switch (piece.type) {
                        case QUEEN -> simPieces.add(new Queen(currentColor, activeP.col, activeP.row));
                        case ROOK -> simPieces.add(new Rook(currentColor, activeP.col, activeP.row));
                        case BISHOP -> simPieces.add(new Bishop(currentColor, activeP.col, activeP.row));
                        case KNIGHT -> simPieces.add(new Knight(currentColor, activeP.col, activeP.row));
                        default -> {
                        }
                    }
                    
                    simPieces.remove(activeP.getIndex());   //removes the pawn
                    copyPieces(simPieces, pieces);  //copy the pieces
                    activeP = null;
                    promotion = false;
                    changePlayerT(); //switch player
                    recomputeCheckForSideToMove();  //checks and resets the checkingP
                    
                }
            }
        }
        
    }
    
    //-------------- CHANGING PPLAYER TURN -----------------------
    private void changePlayerT() {
        
        //restting the players en passant count & changing player turn
        if (currentColor == WHITE) {
            currentColor = BLACK;
            //reset blacks 2step status
            for (Piece piece : pieces) {
                if (piece.color == BLACK) {
                    piece.twoStep = false;
                }
            }
        } else {
            currentColor = WHITE;
            //reset whites 2step status
            for (Piece piece : pieces) {
                if (piece.color == WHITE) {
                    piece.twoStep = false;
                }
            }
        }
        
        activeP = null; //sets activeP tp null
        recomputeCheckForSideToMove();  //checks and resets the checkingP
    }
    
    //------------------ PAINT COMPONENT----------------------
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        board.draw(g2); // draws chessboard

        //for repain, draws everything except active
        for (Piece p : simPieces) {
            if (p != activeP) {
                p.draw(g2);
            }
        }

        if (activeP != null && canMove) {
            final int x = activeP.col * ChessBoard.SQUARE_SIZE;
            final int y = activeP.row * ChessBoard.SQUARE_SIZE;

            // illegal -> red, legal -> yellow (semi-transparent overlay)
            if (isIllegal(activeP) || opponentCanCaptureKing()) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));
                g2.setColor(Color.RED);
                g2.fillRect(x, y, ChessBoard.SQUARE_SIZE, ChessBoard.SQUARE_SIZE);
            } else {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));
                g2.setColor(Color.YELLOW);
                g2.fillRect(x, y, ChessBoard.SQUARE_SIZE, ChessBoard.SQUARE_SIZE);
            }
            g2.setComposite(AlphaComposite.SrcOver); // reset alpha
        }

        //draws the activeP so it sits on top of everthing
        if (activeP != null) {
            activeP.draw(g2);
        }

        //---------------- MESSAGES ----------------------
        g2.setFont(new Font(Font.DIALOG, Font.PLAIN, 30));
        g2.setColor(Color.WHITE);

        if (promotion) {
            g2.drawString("Promote to:", 640 + ChessBoard.HALF_SQUARE_SIZE, 80 + ChessBoard.HALF_SQUARE_SIZE);

            for (Piece piece : promoPieces) {
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row), ChessBoard.SQUARE_SIZE, ChessBoard.SQUARE_SIZE, null);
            }
        } else {
            if (currentColor == WHITE) {
                g2.drawString("White's turn", 640 + ChessBoard.HALF_SQUARE_SIZE, panelHeight - ChessBoard.HALF_SQUARE_SIZE + 15);
                if (checkingP != null && checkingP.color == BLACK) {
                    g2.setColor(Color.RED);
                    g2.drawString("The King", 640 + ChessBoard.HALF_SQUARE_SIZE, 500 - ChessBoard.HALF_SQUARE_SIZE);
                    g2.drawString("is in Check", 640 + ChessBoard.HALF_SQUARE_SIZE, 550 - ChessBoard.HALF_SQUARE_SIZE);
                    g2.setColor(Color.WHITE);
                   
                }
            } else {
                g2.drawString("Black's turn", 640 + ChessBoard.HALF_SQUARE_SIZE, ChessBoard.HALF_SQUARE_SIZE + 15);
                if (checkingP != null && checkingP.color == WHITE) {
                    g2.setColor(Color.RED);
                    g2.drawString("The King", 640 + ChessBoard.HALF_SQUARE_SIZE, 500 - ChessBoard.HALF_SQUARE_SIZE);
                    g2.drawString("is in Check", 640 + ChessBoard.HALF_SQUARE_SIZE, 550 - ChessBoard.HALF_SQUARE_SIZE);
                    g2.setColor(Color.WHITE);
                }
            }
        }

        // --- GAMEOVER ---
        //ChessBoard size
        int square = ChessBoard.SQUARE_SIZE * 8;
        if (gameOver) {
            String s2 = (currentColor == WHITE) ? "By White" : "By Black";
            String s = "CHECKMATE";

            //centering
            FontMetrics fm = g2.getFontMetrics(new Font("Calibri", Font.BOLD, 80));
            int textWidth = fm.stringWidth(s);
            int cx = (square - textWidth) / 2;
            int cy = square / 2 - 12;
            
            g2.setFont(new Font("Calibri", Font.BOLD, 80));
            g2.setColor(new Color(8, 204, 8));
            g2.drawString(s, cx, cy);
            
            FontMetrics fm2 = g2.getFontMetrics(new Font("Calibri", Font.PLAIN, 30));
            int textWidth2 = fm2.stringWidth(s2);
            g2.setFont(new Font("Calibri", Font.PLAIN, 30));
            g2.setColor(new Color(8, 204, 8));
            
            //rough center
            int cx2 = (square - textWidth2) / 2;
            int cy2 = square / 2 + 30;
            g2.drawString(s2, cx2, cy2);
        }
        //stalemate
        if (stalemate) {
            String s2 = "Draw";
            String s = "STALEMATE";

            //centering
            FontMetrics fm = g2.getFontMetrics(new Font("Calibri", Font.BOLD, 80));
            int textWidth = fm.stringWidth(s);
            int cx = (square - textWidth) / 2;
            int cy = square / 2 - 12;
            
            g2.setFont(new Font("Calibri", Font.BOLD, 80));
            g2.setColor(new Color(8, 204, 8));
            g2.drawString(s, cx, cy);
            
            FontMetrics fm2 = g2.getFontMetrics(new Font("Calibri", Font.PLAIN, 30));
            int textWidth2 = fm2.stringWidth(s2);
            g2.setFont(new Font("Calibri", Font.PLAIN, 30));
            g2.setColor(new Color(8, 204, 8));
            
            //rough center
            int cx2 = (square - textWidth2) / 2;
            int cy2 = square / 2 + 30;
            g2.drawString(s2, cx2, cy2);
        }

        g2.dispose();
    }

}
