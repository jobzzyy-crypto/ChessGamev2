/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    private static final Font STATUS_FONT = new Font(Font.DIALOG, Font.PLAIN, 32);
    private boolean whiteTurn = true;   // default; will be set by BoardPanel

    public ScorePanel() {
        setBackground(new Color(0x222222));
        setPreferredSize(new Dimension(50, 50));
        setFont(STATUS_FONT);
    }

    /** BoardPanel calls this whenever the turn flips */
    public void setWhiteTurn(boolean whiteTurn) {
        if (this.whiteTurn != whiteTurn) {
            this.whiteTurn = whiteTurn;
            repaint();                  // triggers paintComponent
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        
    }
}

