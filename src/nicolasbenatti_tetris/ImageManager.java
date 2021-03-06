/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * gestisce le immagini presenti nel gioco
 * @author Nicolas Benatti
 */
public class ImageManager {
    
    public static BufferedImage gameOver;
    public static BufferedImage next;
    public static BufferedImage score;
    public static BufferedImage level;
      
    /**
     * carica le immagini dal disco.
     * @throws java.io.IOException
     */
    public static void loadFromDisk() throws IOException {
        
        gameOver = ImageIO.read(new File("images/gameOver.png"));
        next = ImageIO.read(new File("images/next.png"));
        score = ImageIO.read(new File("images/score.png"));
        level = ImageIO.read(new File("images/level.png"));
    }
}
