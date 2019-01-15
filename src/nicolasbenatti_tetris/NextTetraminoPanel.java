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
 * pannello della GUI che mostra il prossimo tetramino che verrà lanciato
 * @author Nicolas Benatti
 */
public class NextTetraminoPanel extends JPanel {

    /**
     * tipo del tetramino da visualizzare
     */
    private Tetramino tet;
    
    /**
     * colore del tetramino.
     */
    private Color tetColor;
    
    /**
     * colore di sfondo del pannello.
     */
    private Color backgroundColor;
    
    public NextTetraminoPanel() {
        
        setOpaque(true);
        setPreferredSize(new Dimension(150, 150));
        
        tet = null;
        tetColor = null;
        
        backgroundColor = new Color(0, 0, 0);
        setBackground(backgroundColor);
    }
    
    /**
     * permette di dire al pannello il nuovo tetramino da disegnare.
     * @param t tetramino da disegnare
     */
    public void setTetraminoToDraw(Tetramino t) {
        
        tet = t;
        tetColor = MainWindow.decideTetraminoColor(tet.getType());
    }
    
    @Override
    public void paint(Graphics g) {
                
        g.drawImage(ImageManager.next, 0, 90, this);
        
        
        // cancella contenuto precedente
        //g.clearRect(0, 0, getWidth(), getHeight());
        
        //setBackground(backgroundColor);
        
        // scrivi nuovo
        g.setColor(tetColor);
        drawTetramino(g);
    }
    
    
    /**
     * disegna un tetramino sullo schermo
     * @param g contesto grafico
     */
    private void drawTetramino(Graphics g) {
                
        for(int i = 0; i < tet.BBOX_R; ++i) {
            for(int j = 0; j < tet.BBOX_C; ++j) {
                if(tet.getBBval(i, j) != 0) {
                    // riempimento
                    g.setColor(tetColor);
                    
                    g.fillRect(j*MainWindow.tileDim, i*MainWindow.tileDim, MainWindow.tileDim, MainWindow.tileDim);
                    //bordo
                    g.setColor(Color.BLACK);
                    g.drawRect(j*MainWindow.tileDim, i*MainWindow.tileDim, MainWindow.tileDim, MainWindow.tileDim);
                }
            }
        }
    }
}
