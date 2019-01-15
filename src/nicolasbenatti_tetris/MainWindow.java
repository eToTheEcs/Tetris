/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.JFrame;

/**
 * finestra principale del gioco
 * @author Nicolas Benatti
 */
public class MainWindow extends JFrame implements KeyListener {
    
    /**
     * colore di sfondo della schermata
     */
    private final Color backGroundColor = new Color(127, 127, 127);
    
    /* == PARAMETRI DEL GIOCO == */
    
    /**
     * intervallo tra i movimenti di caduta del tetramino (in millisecondi).
     */
    private static final int DELAY_MSEC = 680;
    
    /**
     * no. di righe della griglia.
     */
    public static final int GRID_ROWS = 20;
    /**
     * no. di colonne della griglia.
     */
    public static final int GRID_COLS = 10;
    
    /**
     * larghezza della griglia di gioco (in pixel).
     */
    public static final int w = 350;
    
    /**
     * lato di un "blocco", unità minima del gioco (in pixel).
     */
    public static final int tileDim = w / GRID_COLS;
    
    /**
     * altezza della griglia di gioco (in pixel).
     */
    public static final int h = tileDim * GRID_ROWS;
    
    /* ==================================================*/
    
    /**
     * griglia di gioco.
     */
    private Campo grid;
    
    /**
     * dice al metodo paint() quando bisogna aggiornare la scena statica.<br>
     * la scena statica va aggiornata quando si verifica la fine della caduta di un tetramino
     */
    private static boolean staticRenderNeeded;
    
    /**
     * indica se la caduta di un tetramino è appena iniziata.
     */
    private boolean fallStarted;
    
    /**
     * contiene la direzione dell'ultima mossa effettuata dal giocatore.
     */
    private static int lastMove;
    
    /**
     * precedente orientamento del tetramino.
     * TODO: pensare di spostarlo nella classe Campo
     */
    private Tetramino prevTetramino;
    
    /* == PANNELLI DELL'INTERFACCIA == */
    
    private NextTetraminoPanel ntp;
    
    private GridPanel gp;
    
    private GameDataPanel gdp;
    
    /* =============================== */
    
    /**
     * costruisce la finestra di gioco
     */
    public MainWindow() {
        
        setTitle("TETRIS");
        //setPreferredSize(new Dimension(580, 730));
        setResizable(false);
        setBackground(backGroundColor);
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        grid = new Campo(GRID_ROWS, GRID_COLS);
        ntp = new NextTetraminoPanel();
        gp = new GridPanel(grid);
        gdp = new GameDataPanel(grid.getGm());
                        
        staticRenderNeeded = true;
        lastMove = Direction.DOWN.getValue();
        
        try {
            ImageManager.loadFromDisk();
        }
        catch(IOException e) {
            System.out.println("ERROR: cannot load image resources from disk");
        }
        
        addKeyListener(this);
        
        // aggiungi i pannelli
        c.fill = GridBagConstraints.HORIZONTAL;
        //c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1; 
        c.gridheight = 1;
        c.insets = new Insets(0, 0, 0, 0);
        this.add(ntp, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = c.gridheight = 1;
        c.insets = new Insets(0, 0, 0, 0);
        this.add(gdp, c);
        
        c.gridheight = 3;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 0);
        this.add(gp, c);
        
        this.pack();
        /*grid.throwTetramino();
        ntp.setTetraminoToDraw(grid.getNextTetramino());*/
    }

    public static int getLastMove() {
        return lastMove;
    }

    public static boolean isStaticRenderNeeded() {
        return staticRenderNeeded;
    }
    
    /**
     * ciclo principale del gioco.
     */
    public void gameLoop() {
        
        System.out.println("inizia game loop");
       
        fallStarted = true;
        
        grid.throwTetramino();
        ntp.setTetraminoToDraw(grid.getNextTetramino());
        
        int gameCycles = 0;   // contatore di tetramini caduti
        
        while(!grid.isGameEnded()) {
            
            if(!grid.fallingTetraminoIsColliding(Direction.DOWN)) {
                    
                grid.continueFalling();

                System.out.println("** staticRender: " + staticRenderNeeded + ", fallStarted: " + fallStarted + "**");
                
                // se nel frame prima ho aggiornato la scena statica, adesso non devo più farlo
                if(gameCycles > 0 && staticRenderNeeded && fallStarted) {  
                    System.out.println("azzero " + staticRenderNeeded);
                    this.repaint();
                    fallStarted = false;
                    staticRenderNeeded = false;
                }

                lastMove = Direction.DOWN.getValue();

                try {
                    // salvo posizione precedente del tetramino
                    grid.setPrevTetramino((Tetramino)grid.getFallingTetramino().getSecond().clone());
                }
                catch(CloneNotSupportedException e) {
                    System.out.println(e);
                }
            }
            else {    // il tetramino diventa parte della scena statica e ne viene generato un'altro
                
                //System.out.println("gioco");
                gameCycles++;
                grid.blockFallingTetramino();
                grid.throwTetramino();
                
                int numOfLinesCleared;
                    
                if((numOfLinesCleared = grid.clearLines()) > 0) {
                    // aggiorna punteggio
                    for(int i = 0; i < numOfLinesCleared; ++i)
                        grid.getGm().notifyLineClear();
                    grid.getGm().scoreUp();
                }
                
                ntp.setTetraminoToDraw(grid.getNextTetramino());
                staticRenderNeeded = true;
                fallStarted = true;
            }
            
            this.repaint();
            
            try {
                Thread.sleep(DELAY_MSEC);
            }
            catch(InterruptedException e) {
                System.out.println("pausa interrotta");
            }
        }
    }
    
    @Override
    public void paint(Graphics g) {
                    
        if(grid.isGameEnded()) {
            
            g.drawImage(ImageManager.gameOver, 25, 450, this);
            
            return;
        }
        
        gp.repaint();
        
        // disegna il campo "prossimo tetramino lanciato"
        
        ntp.repaint();
        
        // disegna il pannello dei punteggi
        
        gdp.repaint();
    }
    
    
    /**
     * disegna un tetramino sullo schermo (DEPRECATED)
     * @param g contesto grafico
     * @param anchor punto in alto-sx della bounding box
     * @param tet tetramino da disegnare
     */
    public static void drawTetramino(Graphics g, Punto anchor, Tetramino tet) {
                
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
     * ritorna il colore del tetramino da renderizzare.
     * @param tt tipo del tetramino da renderizzare.
     * @return colore appropriato.
     */
    public static Color decideTetraminoColor(TetraminoType tt) {
        
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
                    
                    /*if(staticRenderNeeded)  
                        staticRenderNeeded = false;*/
                    
                    lastMove = Direction.SX.getValue();
                    
                    try {
                        // salvo posizione precedente del tetramino
                        grid.setPrevTetramino((Tetramino)grid.getFallingTetramino().getSecond().clone());
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
                        grid.setPrevTetramino((Tetramino)grid.getFallingTetramino().getSecond().clone());
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
                        grid.setPrevTetramino((Tetramino)grid.getFallingTetramino().getSecond().clone());
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
                    
                    /*if(staticRenderNeeded)  
                        staticRenderNeeded = false;*/
                    
                    lastMove = Direction.DX.getValue();
                    
                    try {
                        // salvo posizione precedente del tetramino
                        grid.setPrevTetramino((Tetramino)grid.getFallingTetramino().getSecond().clone());
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
                    /*if(staticRenderNeeded)  
                        staticRenderNeeded = false;*/
                    
                    lastMove = Direction.DOWN.getValue();
                    
                    try {
                        // salvo posizione precedente del tetramino
                        grid.setPrevTetramino((Tetramino)grid.getFallingTetramino().getSecond().clone());
                    }
                    catch(CloneNotSupportedException e) {
                        System.out.println(e);
                    }
                }
                else {    // il tetramino diventa parte della scena statica e ne viene generato un'altro
                    //System.out.println("freccia");
                    grid.blockFallingTetramino();
                    grid.throwTetramino();
                    
                    int numOfLinesCleared;
                    
                    if((numOfLinesCleared = grid.clearLines()) > 0) {
                        // aggiorna punteggio
                        for(int i = 0; i < numOfLinesCleared; ++i)
                            grid.getGm().notifyLineClear();
                        grid.getGm().scoreUp();
                    }
                    
                    ntp.setTetraminoToDraw(grid.getNextTetramino());
                    
                    staticRenderNeeded = true;
                    fallStarted = true;
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
