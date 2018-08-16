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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 * finestra del gioco
 * @author Nicolas Benatti
 */
public class MainWindow extends JFrame implements KeyListener {
    
    /**
     * intervallo tra i movimenti di caduta del tetramino (in millisecondi).
     */
    private static final int DELAY_MSEC = 800;
    
    /**
     * no. di righe della griglia.
     */
    private final int GRID_ROWS = 20;
    /**
     * no. di colonne della griglia.
     */
    private final int GRID_COLS = 10;
    
    /**
     * griglia di gioco.
     */
    private Campo grid;
    
    /* === punti di ancoraggio dei componenti dell'interfaccia === */
    
    /**
     * punto di ancoraggio della griglia di gioco.
     */
    private Punto gridAnchor = new Punto(0, 200);    // punto in alto a sx della griglia di gioco
    
    /**
     * punto di ancoraggio dell'indicatore "prossimo tetramino". (in pixel)
     */
    private Punto nextTetraminoAnchor = new Punto(400, 30);
    
    /**
     * punto di ancoraggio dell'indicatore di punteggio
     */
    private Punto scoreAnchor = new Punto(600, 600);

    /**
     * dice al metodo paint() quando bisogna aggiornare la scena statica.<br>
     * la scena statica va aggiornata quando si verifica la fine della caduta di un tetramino
     */
    private boolean staticRenderNeeded;
    
    /**
     * contiene la direzione dell'ultima mossa effettuata dal giocatore.
     */
    private int lastMove;
    
    /**
     * posizione precedente del tetramino
     * TODO: pensare di spostarlo nella classe Campo
     */
    private Tetramino prevTetramino;
    
    /**
     * costruisce la finestra di gioco
     */
    public MainWindow() {
        
        setTitle("TETRIS");
        setPreferredSize(new Dimension(800, 780));
        setResizable(false);
        
        grid = new Campo(GRID_ROWS, GRID_COLS, 350);
        
        addKeyListener(this);
        
        // lancia il primo tetramino
        grid.throwTetramino();
        
        System.out.println(grid.getFallingTetramino().getSecond());
                
        staticRenderNeeded = true;
        lastMove = Direction.DOWN.getValue();
        
        // inizializzazione casuale
        prevTetramino = new Tetramino(TetraminoType.T);
    }
    
    /**
     * esegue il ciclo principale del gioco.
     */
    public void gameLoop() {
        
        System.out.println("inizia game loop");
        
        boolean gameStarted = true;
        
        while(!grid.gameOver()) {
            
            if(!grid.fallingTetraminoIsColliding(Direction.DOWN)) {
                    
                grid.continueFalling();

                System.out.println("**" + staticRenderNeeded + ", " + gameStarted + "**");
                
                // se nel frame prima ho aggiornato la scena statica, adesso non devo più farlo
                if(staticRenderNeeded && !gameStarted) {  
                    this.repaint();
                    staticRenderNeeded = false;
                    gameStarted = true;
                }

                lastMove = Direction.DOWN.getValue();

                try {
                    // salvo posizione precedente del tetramino
                    this.prevTetramino = (Tetramino)grid.getFallingTetramino().getSecond().clone();
                }
                catch(CloneNotSupportedException e) {
                    System.out.println(e);
                }
            }
            else {    // il tetramino diventa parte della scena statica e ne viene generato un'altro

                grid.blockFallingTetramino();
                grid.throwTetramino();
                staticRenderNeeded = true;
            }
            
            try {
                Thread.sleep(DELAY_MSEC);
            }
            catch(InterruptedException e) {
                System.out.println("pausa interrotta");
            }
            
            this.repaint();
        }
    }
    
    @Override
    public void paint(Graphics g) {
                
        System.out.println(staticRenderNeeded);
        
        if(staticRenderNeeded) {
            
            /* === disegna la griglia di gioco === */
            g.fillRect(gridAnchor.getJ(), gridAnchor.getI(), grid.getW(), grid.getH());
            
            /* === disegna la scena statica === */
            
            for(int i = 0; i < GRID_ROWS; ++i) {
               for(int j = 0; j < GRID_COLS; ++j) {

                   int cellValue = grid.getCellValue(i, j);

                   if(cellValue == 0) {
                       g.setColor(Color.BLACK);
                   }
                   else {
                       g.setColor(decideTetraminoColor(TetraminoType.values()[cellValue - 1]));
                   }
                   
                   // passa da indici a coordinate-schermo
                   Punto realCoordinates = indexToScreen(i, j);

                   g.fillRect(realCoordinates.getJ(), realCoordinates.getI(), grid.getTileDim(), grid.getTileDim());
                   
                   // disegna contorno cella (per visualizzazione griglia
                   g.setColor(Color.BLACK);
                   g.drawRect(realCoordinates.getJ(), realCoordinates.getI(), grid.getTileDim(), grid.getTileDim());
               }
            }
        }
        
        /* === disegna il tetramino in movimento === */
        
        Punto tetraminoAnchor = grid.getFallingTetramino().getFirst();
        Tetramino tetramino = grid.getFallingTetramino().getSecond();
        
        // punti di ancoraggio precedenti
        int prevI = tetraminoAnchor.getI(), prevJ = tetraminoAnchor.getJ();
       
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
        
        //Punto prevTetraminoAnchor = new Punto(prevI, prevJ);
        Punto prevTetraminoScreenCoords = indexToScreen(prevI, prevJ);
                
        removeTetramino(g, prevTetraminoScreenCoords, this.prevTetramino);
        
        // disegnalo nella nuova posizione
        g.setColor(decideTetraminoColor(tetramino.getType()));
        drawTetramino(g, indexToScreen(tetraminoAnchor.getI(), tetraminoAnchor.getJ()), tetramino);
        
        /* === disegna il campo "prossimo tetramino lanciato" === */
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("NEXT", nextTetraminoAnchor.getJ(), nextTetraminoAnchor.getI() - 20);
        g.setColor(getBackground());
        
        // cancella precedente
        g.fillRect(nextTetraminoAnchor.getJ(), nextTetraminoAnchor.getI(), grid.getTileDim() * 4, grid.getTileDim() * 4);
        g.drawRect(nextTetraminoAnchor.getJ(), nextTetraminoAnchor.getI(), grid.getTileDim() * 4, grid.getTileDim() * 4);
        
        // scrivi nuovo
        g.setColor(decideTetraminoColor(grid.getNextTetramino().getType()));
        drawTetramino(g, nextTetraminoAnchor, grid.getNextTetramino());
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
        
        return new Punto(gridAnchor.getI() + grid.getTileDim()*i + 27, gridAnchor.getJ() + grid.getTileDim()*j);
    }
    
    /**
     * disegna un tetramino sullo schermo
     * @param g contesto grafico
     * @param anchor punto in alto-sx della bounding boc
     * @param tet tetramino da disegnare
     */
    public void drawTetramino(Graphics g, Punto anchor, Tetramino tet) {
        
        int tileDim = grid.getTileDim();
        
        for(int i = 0; i < tet.BBOX_R; ++i) {
            for(int j = 0; j < tet.BBOX_C; ++j) { 
                if(tet.getBBval(i, j) != 0) {
                    g.setColor(decideTetraminoColor(tet.getType()));
                    g.fillRect(anchor.getJ()+(j*tileDim), anchor.getI()+(i*tileDim), tileDim, tileDim);
                    
                    g.setColor(Color.BLACK);
                    g.drawRect(anchor.getJ()+(j*tileDim), anchor.getI()+(i*tileDim), tileDim, tileDim);
                }
            }
        }
    }
    
    /**
     * cancella il tetramino dallo schermo.
     * @param g contesto grafico
     * @param anchor punto in alto-sx della bounding boc
     * @param tet tetramino da disegnare
     */
    public void removeTetramino(Graphics g, Punto anchor, Tetramino tet) {
                
        int tileDim = grid.getTileDim();
        
        for(int i = 0; i < tet.BBOX_R; ++i) {
            for(int j = 0; j < tet.BBOX_C; ++j) { 
                if(tet.getBBval(i, j) != 0) {
                    g.setColor(Color.BLACK);
                    g.fillRect(anchor.getJ()+(j*tileDim), anchor.getI()+(i*tileDim), tileDim, tileDim);
                }
            }
        }
    }
    
    /**
     * ritorna il colore del tetramino da renderizzare.
     * @param tt tipo del tetramino da renderizzare.
     * @return colore appropriato.
     */
    private Color decideTetraminoColor(TetraminoType tt) {
        
        Color res = null;
        
        switch(tt) {
            
            case I:
                res = new Color(170, 0, 0);
                break;
            case O:
                res = new Color(0, 0, 170);
                break;
            case T:
                res = new Color(170, 85, 0);
                break;
            case S:
                res = new Color(0, 170, 0);
                break;
            case Z:
                res = new Color(0, 170, 170);
                break;
            case J:
                res = new Color(170, 170, 170);
                break;
            case L:
                res = new Color(170, 0, 170);
                break;
        }
        
        return res;
    }
    
    
    @Override
    public void keyPressed(KeyEvent ke) {
                
        // gestione dei comandi
        switch(ke.getKeyCode()) {
            
            case 37:    // left arrow
                
                if(!grid.fallingTetraminoIsColliding(Direction.SX)) {
                    
                    grid.slideFallingTetramino(Direction.SX);
                    
                    if(staticRenderNeeded)  
                        staticRenderNeeded = false;
                    
                    lastMove = Direction.SX.getValue();
                    
                    try {
                        // salvo posizione precedente del tetramino
                        this.prevTetramino = (Tetramino)grid.getFallingTetramino().getSecond().clone();
                    }
                    catch(CloneNotSupportedException e) {
                        System.out.println(e);
                    }
                }
                
                this.repaint();
                
                break;
                
            case 38:    // up arrow / X
            case KeyEvent.VK_X:
                
                if(grid.getFallingTetramino().getSecond().getType() != TetraminoType.O && grid.canRotateFallingTetramino()) {
                    
                    try {
                        // salvo posizione precedente del tetramino
                        this.prevTetramino = (Tetramino)grid.getFallingTetramino().getSecond().clone();
                    }
                    catch(CloneNotSupportedException e) {
                        System.out.println(e);
                    }
                    
                    grid.getFallingTetramino().getSecond().rotateClockwise();
                    lastMove = 3;
                }
                
                this.repaint();
                
                break;
                
            case KeyEvent.VK_Z:     // Z
                
                if(grid.getFallingTetramino().getSecond().getType() != TetraminoType.O && grid.canRotateFallingTetramino()) {
                    
                    try {
                        // salvo posizione precedente del tetramino
                        this.prevTetramino = (Tetramino)grid.getFallingTetramino().getSecond().clone();
                    }
                    catch(CloneNotSupportedException e) {
                        System.out.println(e);
                    }
                    
                    grid.getFallingTetramino().getSecond().rotateAntiClockwise();
                    lastMove = 4;
                }
                
                this.repaint();
                
                break;
                
            case 39:    // right arrow
                
                if(!grid.fallingTetraminoIsColliding(Direction.DX)) {
                    
                    grid.slideFallingTetramino(Direction.DX);
                    
                    if(staticRenderNeeded)  
                        staticRenderNeeded = false;
                    
                    lastMove = Direction.DX.getValue();
                    
                    try {
                        // salvo posizione precedente del tetramino
                        this.prevTetramino = (Tetramino)grid.getFallingTetramino().getSecond().clone();
                    }
                    catch(CloneNotSupportedException e) {
                        System.out.println(e);
                    }
                }
                
                this.repaint();
                
                break;
            
            case 40:    // down arrow
                
                if(!grid.fallingTetraminoIsColliding(Direction.DOWN)) {
                    
                    grid.continueFalling();
                    
                    // se nel frame prima ho aggiornato la scena statica, adesso smetto
                    if(staticRenderNeeded)  
                        staticRenderNeeded = false;
                    
                    lastMove = Direction.DOWN.getValue();
                    
                    try {
                        // salvo posizione precedente del tetramino
                        this.prevTetramino = (Tetramino)grid.getFallingTetramino().getSecond().clone();
                    }
                    catch(CloneNotSupportedException e) {
                        System.out.println(e);
                    }
                }
                else {    // il tetramino diventa parte della scena statica e ne viene generato un'altro
                    
                    grid.blockFallingTetramino();
                    grid.throwTetramino();
                    staticRenderNeeded = true;
                }
                
                this.repaint();
                
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {}
    
    @Override
    public void keyReleased(KeyEvent ke) {}
}
