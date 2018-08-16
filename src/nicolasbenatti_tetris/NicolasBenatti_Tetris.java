/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * main class
 * @author Nicolas Benatti
 */
public class NicolasBenatti_Tetris {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        MainWindow mw = new MainWindow();
        
        mw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mw.setLocationRelativeTo(null);
        mw.pack();
        mw.setVisible(true);
        
        // fai partire il gioco
        mw.gameLoop();
        
        /*Tetramino t = new Tetramino(TetraminoType.S);
        
        for(int i = 0; i < 2; ++i) {
            t.rotateClockwise();
        }
        
        System.out.println(t);
        
        ArrayList<Punto> lowerBound = t.getLateralBound(Direction.SX);
        
        System.out.println(lowerBound);*/
    }    
}
