/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

import java.util.ArrayList;

/**
 * gestisce griglia di gioco e la partita.
 * @author Nicolas Benatti
 */
public class Campo {
    
    /**
     * riga dalla quale tutti i tetramini vengono lanciati.<br>
     * l'indice di colonna è invece variabile.
     */
    private static int INITIAL_SPAWN_ROW = 0;
    
    /**
     * contiene l'indice di colonna dal quale l'ultimo tetramino è stato lanciato
     */
    private int lastSpawnCol;
    
    /**
     * numero di righe e colonne della griglia.
     */
    private int cols, rows;

    /**
     * tetramino che sta cadendo:<br>
     * la coppia contiene il punto alto-sx della bb
     * e il tetramino stesso.
     */
    private Pair<Punto, Tetramino> fallingTetramino;  
    
    /**
     * tetramino precedente.
     */
    private Tetramino prevTetramino;
    
    /**
     * prossimo tetramino, generato mentre <br>
     * l'altro sta cadendo.
     */
    private Tetramino nextTetramino;
    
    /**
     * griglia numerica rappresentante<br>
     * la situazione di gioco:
     * se la cella contiene il no. 0, è vuota<br>
     * altrimenti la cella contiene un no. corrispondente al tetramino che la occupa.
     */
    private byte[][] grid;
    
    /**
     * indica lo stato della partita
     * 0: in corso
     * 1: terminata
     */
    private boolean gameStatus;
    
    /**
     * gestore del punteggio della partita
     */
    private GameManager gm;
    
    /**
     * costruisci una griglia di gioco
     * @param rows numero di righe
     * @param cols numero di colonne
     */
    public Campo(int rows, int cols) {
                
        this.cols = cols;
        this.rows = rows;
        
        grid = new byte[this.rows][this.cols];
        
        for(int i = 0; i < grid.length; ++i) {
            for(int j = 0; j < grid[0].length; ++j) {
                grid[i][j] = 0;
            }
        }
        
        // inizializzazioni casuali
        fallingTetramino = new Pair(new Punto(0, 0), new Tetramino(TetraminoType.T));
        nextTetramino = null;
        prevTetramino = new Tetramino(TetraminoType.T);
        
        gameStatus = false;
        
        gm = new GameManager();
    }

    public GameManager getGm() {
        return gm;
    }

    public boolean isGameEnded() {
        return gameStatus;
    }
    
    /**
     * controlla se la condizione di fine partita è soddisfatta.<br>
     * tale controllo viene eseguito prima di lanciare i tetramini.
     * @return 
     */
    private boolean checkGameEnd(int spawnCol) {
                
        Tetramino toThrow = fallingTetramino.getSecond();
        
        ArrayList<Punto> lb = /*toThrow.getLowerBound()*/toThrow.getBound(Direction.DOWN);
        
        int i, minI = grid.length;
        
        for(Punto it : lb) {
            
            for(i = 0; i < grid.length; ++i) {
                
                if(grid[i][spawnCol + it.getJ()] != 0 && i < minI) {
                    
                    minI = i;
                    break;
                }
            }
        }
        
        return minI < TetraminoBuilder.tetraminoHeights.get(TetraminoType.O);
    }
    
    /**
     * ritorna il numero di righe della griglia
     * @return numero righe griglia
     */
    public int getCols() {
        return cols;
    }

    /**
     * ritorna il numero di colonne della griglia
     * @return numero colonne griglia
     */
    public int getRows() {
        return rows;
    }

    /**
     * ritorna il tetramino in fase di caduta.
     * @return coppia contenente punto di ancoraggio e tetramino.
     */
    public Pair<Punto, Tetramino> getFallingTetramino() {
        return fallingTetramino;
    }
    
    /**
     * ritorna il tetramino successivo a quello in caduta.
     * @return il tetramino successivo a quello in caduta
     */
    public Tetramino getNextTetramino() {
        
        return nextTetramino;
    }
    
    public Tetramino getPrevTetramino() {
        return prevTetramino;
    }
    
    public void setPrevTetramino(Tetramino t) throws CloneNotSupportedException {
        
        prevTetramino = (Tetramino)t.clone();
    }
    
    /**
     * controlla se il giocatore ha riempito delle linee
     * @return indici di riga delle linee complete.
     */
    private ArrayList<Integer> checkForLines() {
        
        ArrayList<Integer> completedLines = new ArrayList<>();
        
        int filledCells = 0;    // no. di celle occupate nella riga
        
        for(int i = 0; i < grid.length; ++i) {
            filledCells = 0;
            for(int j = 0; j < grid[i].length; ++j) {
                
                if(grid[i][j] != 0)
                    filledCells++;
            }
            
            if(filledCells == grid[i].length)
                completedLines.add(i);
        }
        
        return completedLines;
    }
    
    /**
     * toglie, se presenti, le linee complete dalla scena di gioco.
     * 
     * @return no. di linee cancellate
     */
    public int clearLines() {
        
        ArrayList<Integer> lines = checkForLines();
        
        for(int it : lines) {
            
            for(int j = 0; j < grid[it].length; ++j) {
                for(int i = it; grid[i][j] != 0; --i) {
                    
                    grid[i][j] = grid[i-1][j];
                }
            }
        }
        
        return lines.size();
    }
    
    /**
     * genera un tetramino casuale
     * @return tetramino generato
     */
    private Tetramino generateRandomTetramino() {
        
        int numOfTetraminoes = TetraminoType.values().length;
        
        return new Tetramino(TetraminoType.values()[ (int)(Math.random() * numOfTetraminoes) ]);
    }
    
    /**
     * fa cadere un nuovo tetramino generato casualmente<br>
     * ma allo stesso tempo genera il tetramino che cadrà successivamente ad esso.
     */
    public void throwTetramino() {
        
        Tetramino toThrow = (nextTetramino == null) ? generateRandomTetramino() : nextTetramino;
        
        lastSpawnCol = generateSpawnIndex(toThrow);
        
        // resetta i dati sul nuovo tetramino
        this.fallingTetramino.setFirst(new Punto(INITIAL_SPAWN_ROW, lastSpawnCol));
        this.fallingTetramino.setSecond(toThrow);      
        
        if(checkGameEnd(lastSpawnCol)) {
            System.out.println("PARTITA FINITA");
            gameStatus = true;
        }
        else
            System.out.println("PARTITA IN CORSO");
        
        // genera il 2° tetramino
        this.nextTetramino = generateRandomTetramino();
    }
    
    /**
     * fa proseguire la caduta del tetramino
     */
    public void continueFalling() {
        
        // scrivi il tetramino nella nuova posizione
        Punto newLocation = fallingTetramino.getFirst();
        newLocation.setI(newLocation.getI()+1);
        fallingTetramino.setFirst(newLocation);
    }
    
    /**
     * "scrive" il tetramino sulla matrice numerica.
     * @param tet tetramino da disegnare
     * @param anchor punto da cui disegnare
     */
    private void writeTetraminoOnGrid(Tetramino tet, Punto anchor) {
        
        for(int i = anchor.getI(); i < anchor.getI() + tet.BBOX_R; ++i) {
            for(int j = anchor.getJ(); j < anchor.getJ() + tet.BBOX_C; ++j) {
                if(tet.getBBval(i - anchor.getI(), j - anchor.getJ()) != 0) {
                    grid[i][j] = tet.getType().getValue();
                }
            }
        }
    }
    
    /**
     * cancella il tetramino dalla matrice numerica.
     * @param tet
     * @param anchor
     */
    private void deleteTetraminoFromGrid(Tetramino tet, Punto anchor) {
        
        for(int i = anchor.getI(); i < anchor.getI() + tet.BBOX_R; ++i) {
            for(int j = anchor.getJ(); j < anchor.getJ() + tet.BBOX_C; ++j) {
                grid[i][j] = 0;
            }
        }
    }
    
    /**
     * scrive il tetramino nella scena statica
     */
    public void blockFallingTetramino() {
        
        writeTetraminoOnGrid(fallingTetramino.getSecond(), fallingTetramino.getFirst());
    }
    
    /**
     * trasla il tetramino lungo l'asse x
     * @param dir direzione (dx o sx)
     */
    public void slideFallingTetramino(Direction dir) {
        
        Punto newLocation = fallingTetramino.getFirst();
        
        if(dir == Direction.DX) {
            
            newLocation.setJ(newLocation.getJ()+1);
        }
        else {
            newLocation.setJ(newLocation.getJ()-1);
        }
    }
    
    /**
     * genera l'indice di colonna da cui il nuovo tetramino verrà lanciato
     * @param t tetramino da lanciare
     * @return indice di colonna della griglia di gioco
     */
    private int generateSpawnIndex(Tetramino t) {
        
        return (int)(Math.random() * ((this.cols - t.BBOX_C) + 1));
    }
    
    /**
     * dice quando il tetramino corrente sta collidendo con i tetramini già caduti<br>
     * o con il suolo.
     * @param dir direzione in cui verificare la collisione
     * @return true se il tetramino sta collidendo, false altrimenti
     */
    public boolean fallingTetraminoIsColliding(Direction dir) {
        
        ArrayList<Punto> bound = null;
        
        /*if(dir == Direction.DOWN)
            bound = fallingTetramino.getSecond().getLateralBound(Direction.DOWN);
        else if(dir == Direction.DX)
            bound = fallingTetramino.getSecond().getLateralBound(Direction.DX);
        else
            bound = fallingTetramino.getSecond().getLateralBound(Direction.SX);*/
        
        bound = fallingTetramino.getSecond().getBound(dir);
        
        int baseI = fallingTetramino.getFirst().getI();
        int baseJ = fallingTetramino.getFirst().getJ();
        
        if(dir == Direction.DOWN && baseI + bound.get(0).getI() >= grid.length - 1 ||
           dir == Direction.DX && baseJ + bound.get(0).getJ() >= grid[0].length - 1 ||
           dir == Direction.SX && baseJ + bound.get(0).getJ() <= 0)
            return true;
                
        for(Punto boundTile : bound) {
            
            // coordinate in cui verificare la presenza di collisioni
            int iCoord = 0, jCoord = 0;
            
            if(dir == Direction.DOWN) {
                iCoord = baseI + boundTile.getI() + 1;
                jCoord = baseJ + boundTile.getJ();
            }
            else if(dir == Direction.DX) {
                iCoord = baseI + boundTile.getI();
                jCoord = baseJ + boundTile.getJ() + 1;
            }
            else {
                iCoord = baseI + boundTile.getI();
                jCoord = baseJ + boundTile.getJ() - 1;
            }
            
            // ho rilevato collisione
            if(grid[iCoord][jCoord] != 0) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * ritorna il valore di una cella della griglia
     * @param i indice di riga
     * @param j indice di colonna
     * @return valore della cella:<br>
     * 0 se vuota<br>
     * T se occupata da un tetramino di tipo T
     * @see TetraminoType per i tipi di tetramini e i loro valori interi
     */
    public int getCellValue(int i, int j) {
        
        if(i < 0 || j < 0 || i >= grid.length || j >= grid[0].length)
            throw new OutOfGridException("Campo::getCellValue(): specified coordinates are out of the grid");
        
        return grid[i][j];
    }
    
    /**
     * dice se è possibile ruotare il tetramino in caduta.
     * @return true se si può ruotare, false altrimenti
     */
    public boolean canRotateFallingTetramino() {
        
        //boolean res = false;
        
        int anchorI = fallingTetramino.getFirst().getI();
        int anchorj = fallingTetramino.getFirst().getJ();
        
        // controlla se ci sono blocchi all'interno dell'area di contenimento
        for(int i = anchorI; i < anchorI + fallingTetramino.getSecond().BBOX_R; ++i) {
            for(int j = anchorj; j < anchorj + fallingTetramino.getSecond().BBOX_C; ++j) {
                
                if(i < 0 || j < 0 || i >= grid.length || j >= grid[0].length)
                    return false;
                
                if(grid[i][j] != 0)
                    return false;
            }
        }
        
        return true;
    }
}
