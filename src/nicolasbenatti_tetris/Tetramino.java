/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

import java.util.ArrayList;

/**
 * figura composta da 4 quadrati collegati, posizionati a piacere.
 * @author Nicolas Benatti
 */
public class Tetramino implements Cloneable {
    
    /**
     * no. di colonne della matrice<br>
     * che contiene il tetramino (bounding box)
     */
    public final int BBOX_R;
    
    /**
     * no. di righe della matrice<br>
     * che contiene il tetramino (bounding box)
     */
    public final int BBOX_C;
    
    /**
     * il tipo di tetramino
     * @see TetraminoType
     */
    private TetraminoType type;
    
    /**
     * contenuto della bounding box
     */
    private byte[][] coords;
        
    public Tetramino(TetraminoType type) {
        
        this.type = type;
        
        if(this.type.equals(TetraminoType.I))
            BBOX_C = BBOX_R = 4;
        else
            BBOX_C = BBOX_R = 3;
        
        // costruisci tetramino (valutare il fatto di rendere la classe non statica)
        coords = TetraminoBuilder.build(type);
    }

    public Tetramino(Tetramino other) {
        
        this.BBOX_R = other.BBOX_R;
        this.BBOX_C = other.BBOX_C;
        
        for(int i = 0; i < other.BBOX_R; ++i)
            for(int j = 0; j < other.BBOX_C; ++j)
                this.coords[i][j] = other.coords[i][j];
        
        this.type = other.type;
    }
    
    /**
     * ritorna il tipo del tetramino
     * @return tipo (Enum) del tetramino
     */
    public TetraminoType getType() {
        return type;
    }
    
    /**
     * ritorna il contenuto della bb all'indice specificato
     * @param i indice di riga
     * @param j indice di colonna
     * @return valore all'indice specificato (vuoto o tetramino)
     */
    public int getBBval(int i, int j) {
        
        return coords[i][j];
    }
    
    /**
     * ruota il tetramino in senso orario
     */
    public void rotateClockwise() {
        
        byte[][] newCoords  = new byte[BBOX_R][BBOX_C];
        
        for(int i = 0; i < coords.length; ++i) {
            for(int j = 0; j < coords[0].length; ++j) {
                
                newCoords[i][j] = coords[coords.length - j - 1][i];
            }
        }
        
        coords = newCoords;
    }
    
    /**
     * ruota il tetramino in senso antiorario
     */
    public void rotateAntiClockwise() {
        
        for(int i = 0; i < 3; ++i)
            rotateClockwise();
    }

    /**
     * ritorna il/i blocchetto/i più in basso del tetramino<br>
     * utile per determinare la collisione con altri tetramini
     * @return coordinate del blocchetto
     */
    public ArrayList<Punto> getLowerBound() {
        
        ArrayList<Punto> lowerBound = new ArrayList<>();
        //lowerBound.add(new Punto(0, 0));
        
        for(int i = 0; i < this.BBOX_R; ++i) {
            for(int j = 0; j < this.BBOX_C; ++j) {
                if(coords[i][j] != 0) {
                    /*if(i > lowerBound.get(0).getI()) {
                        lowerBound.clear();
                        lowerBound.add(new Punto(i, j));
                    }
                    else if(i == lowerBound.get(0).getI()) {
                        lowerBound.add(new Punto(i, j));
                    }*/
                    // prendi solo i blocchi che costituiscono il bordo basso
                    if(i == BBOX_R - 1 || coords[i+1][j] == 0) {
                        lowerBound.add(new Punto(i, j));
                    }
                }
            }
        }
        
        lowerBound.sort(new PuntoCompRow());   // ordina secondo indice i decrescente
        
        return lowerBound;
    }
    
    /**
     * ritorna il/i blocchetto/i più in basso del tetramino<br>
     * utile per determinare la collisione con altri tetramini
     * @param dir direzione nella quale calcolare il bound
     * @return coordinate del blocchetto
     */
    public ArrayList<Punto> getLateralBound(Direction dir) {
        
        ArrayList<Punto> lateralBound = new ArrayList<>();
        //lateralBound.add(new Punto(0, 0));
        
        boolean cond;
        int colIndexToWatch;    // indice di colonna da guardare per capire se il blocchetto fa parte del bordo
        
        for(int i = 0; i < this.BBOX_R; ++i) {
            for(int j = 0; j < this.BBOX_C; ++j) {
                
                if(coords[i][j] != 0) {
                    
                    /*cond = (dir == Direction.DX) ?  (j > lateralBound.get(0).getJ()) : (j < lateralBound.get(0).getJ());
                    
                    if(cond) {
                        lateralBound.clear();
                        lateralBound.add(new Punto(i, j));
                    }
                    else if(j == lateralBound.get(0).getJ()) {
                        lateralBound.add(new Punto(i, j));
                    }*/
                    
                    cond = (dir == Direction.DX) ? (j == BBOX_C - 1) : (j == 0);
                    colIndexToWatch = (dir == Direction.DX) ? j + 1 : j - 1;
                    
                    if(cond || coords[i][colIndexToWatch] == 0) {
                        lateralBound.add(new Punto(i, j));
                    }
                    
                }
            }
        }
        
        lateralBound.sort(new PuntoCompCol());
        
        return lateralBound;
    }
    
    @Override
    public String toString() {
        String res = "";
        
        for(int i = 0; i < coords.length; ++i) {
            for(int j = 0; j < coords[0].length; ++j) {
                
                res += (coords[i][j] == 0) ? "." : "#";
            }
            res += "\n";
        }
        
        return res;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        
        try {
            Tetramino cloned = (Tetramino)super.clone();
            
            cloned.coords = (byte[][])coords.clone();
            
            return cloned;
        }
        catch(CloneNotSupportedException e) {
            return null;
        }
    }
}
