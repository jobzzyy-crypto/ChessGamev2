/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Jayden Tosul
 */
public class Mouse extends MouseAdapter {
    
    public int x, y;    //gets the mouse coordinates
    public boolean pressed;
    
    @Override
    public void mousePressed(MouseEvent e) {
//        System.err.println("mousePressed");
        pressed = true;
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        pressed = false;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
    
}
