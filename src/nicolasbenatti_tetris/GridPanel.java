/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * pannello con che disegna la griglia di gioco
 * @author Nicolas Benatti
 */
public class GridPanel extends JPanel {

    /**
     * colore di sfondo del pannello
     */
    private final Color backgroundColor;

    /**
     * campo di gioco.
     */
    private Campo gameField;
    
    /**
     * costruisce un campo di gioco.
     * @param gf contenuto del campo di gioco
     */
    public GridPanel(Campo gf) {
        
        setOpaque(true);
        
        backgroundColor = new Color(0, 0, 0);
        
        // prendi le dimensioni da MainWindow
        
        gameField = new Campo(MainWindow.GRID_ROWS, MainWindow.GRID_COLS);
        
        setPreferredSize(new Dimension(MainWindow.w, MainWindow.h));
        setBackground(backgroundColor);
        
        // aggancio il campo di gioco
        gameField = gf;
    }

    @Override
    public void paint(Graphics g) {
        
        
        System.out.println("PAINT " + MainWindow.isStaticRenderNeeded());
        
        if(MainWindow.isStaticRenderNeeded()) {
                        
            /* === disegna la scena statica === */
            
            for(int i = 0; i < MainWindow.GRID_ROWS; ++i) {
               for(int j = 0; j < MainWindow.GRID_COLS; ++j) {

                   int cellValue = gameField.getCellValue(i, j);
                   
                   if(cellValue == 0) {
                       g.setColor(backgroundColor);
                   }
                   else {
                       g.setColor(MainWindow.decideTetraminoColor(TetraminoType.values()[cellValue - 1]));
                   }
                   
                   Punto realCoordinates = indexToScreen(i, j);

                   g.fillRect(realCoordinates.getJ(), realCoordinates.getI(), MainWindow.tileDim, MainWindow.tileDim);
                   // disegna contorno cella
                   g.setColor(backgroundColor);
                   g.drawRect(realCoordinates.getJ(), realCoordinates.getI(), MainWindow.tileDim, MainWindow.tileDim);
               }
            }
        }
        
        Punto tetraminoAnchor = gameField.getFallingTetramino().getFirst();
        Tetramino tetramino = gameField.getFallingTetramino().getSecond();
        
        // punti di ancoraggio precedenti
        int prevI = tetraminoAnchor.getI(), prevJ = tetraminoAnchor.getJ();
       
        int lastMove = MainWindow.getLastMove();
        
        if(lastMove == Direction.DOWN.getValue()) {
            prevI--;
        }
        else if(lastMove == Direction.DX.getValue()) {
            prevJ--;
        }
        else if(lastMove == Direction.SX.getValue()) {
            prevJ++;
        }
        else {}
        
        //System.out.println("last move: " + lastMove + "\ncoords: " + tetraminoAnchor + "\nprev coords: " + new Punto(prevI, prevJ));
        
        Punto prevTetraminoScreenCoords = indexToScreen(prevI, prevJ);
                
        removeTetramino(g, prevTetraminoScreenCoords, gameField.getPrevTetramino());
        
        // disegnalo nella nuova posizione
        g.setColor(MainWindow.decideTetraminoColor(tetramino.getType()));
        MainWindow.drawTetramino(g, indexToScreen(tetraminoAnchor.getI(), tetraminoAnchor.getJ()), tetramino);
    }
    
    /**
     * converte una coppia di indici della griglia di gioco<br>
     * in coordinate dello schermo, basandosi sulla dimensione di un blocchetto
     * e della griglia
     * @param i indice di riga
     * @param j indice di colonna
     * @return coordinate sullo schermo
     */
    private Punto indexToScreen(int i, int j) {
        
        return new Punto(MainWindow.tileDim*i, MainWindow.tileDim*j);
    }
    
    /**
     * cancella il tetramino dallo schermo. (DEPRECATED)
     * @param g contesto grafico
     * @param anchor punto in alto-sx della bounding box
     * @param tet tetramino da disegnare
     */
    public void removeTetramino(Graphics g, Punto anchor, Tetramino tet) {
        
        for(int i = 0; i < tet.BBOX_R; ++i) {
            for(int j = 0; j < tet.BBOX_C; ++j) { 
                if(tet.getBBval(i, j) != 0) {
                    g.setColor(backgroundColor);
                    g.fillRect(anchor.getJ()+(j*MainWindow.tileDim), anchor.getI()+(i*MainWindow.tileDim), MainWindow.tileDim, MainWindow.tileDim);
                    g.drawRect(anchor.getJ()+(j*MainWindow.tileDim), anchor.getI()+(i*MainWindow.tileDim), MainWindow.tileDim, MainWindow.tileDim);
                }
            }
        }
    }

}
