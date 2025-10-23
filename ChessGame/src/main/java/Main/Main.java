/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package Main;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 *
 * @author Jayden Tosul, Job Rotoava
 */
public class Main {

    public static void main(String[] args) {
        
        int windowWidth = 1050;
        int windowHeight = 1000;
        
        JFrame frame = new JFrame();
        BoardPanel board = new BoardPanel();
        ScorePanel score = new ScorePanel();
        
//        frame.add(score, BorderLayout.NORTH);
        frame.add(board, BorderLayout.CENTER);
        board.launchGame(); //start the gameloop
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth, windowHeight);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
        
    }
}
