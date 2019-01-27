/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * mostra punteggio e livello della partita
 * @author Nicolas Benatti
 */
public class GameDataPanel extends JPanel {

    private final Color backgroundColor;
    
    private GameManager gm;
    
    public GameDataPanel(GameManager gm) {
        
        backgroundColor = new Color(255, 0, 0);
        setPreferredSize(new Dimension(150, 150));
        setOpaque(false);
        setBackground(backgroundColor);
        
        this.gm = gm;
    }
    
    @Override
    public void paint(Graphics g) {
        
        g.drawImage(ImageManager.level, 10, 40, this);
        g.drawImage(ImageManager.score, 10, 80, this);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 18));
        g.drawString(""+gm.getLevel(), 120, 50);
        g.drawString(""+gm.getScore(), 120, 90);
    }
}
