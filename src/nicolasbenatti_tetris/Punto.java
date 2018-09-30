/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

/**
 * punto in una matrice (riga/colonna).
 * @author Nicolas Benatti
 */
public class Punto implements Comparable<Punto>, Cloneable {
    
    /**
     * ascissa del punto
     */
    private int i; 
    
    /**
     * ordinata del punto
     */
    private int j; 
    
    /**
     * costruisce un punto con le sue coordinate
     * @param i indice riga
     * @param j indice colonna
     */
    public Punto(int i, int j) { 
        this.i = i; 
        this.j = j; 
    } 
    
    /**
     * ritorna l'ascissa del punto
     * @return ascissa punto
     */
    public int getI() { 
        return i; 
    } 
    
    /**
     * ritorna l'ordinata del punto
     * @return ordinata punto
     */
    public int getJ() { 
        return j; 
    } 

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }
    
    @Override 
    public boolean equals(Object obj) { 
        
        if (this == obj) 
            return true; 
        if (obj == null) 
            return false; 
        if (getClass() != obj.getClass()) 
            return false; 
        
        Punto other = (Punto) obj; 
        
        if (i != other.i) 
            return false; 
        if (j != other.j) 
            return false; 
        
        return true; 
    } 

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.i;
        hash = 83 * hash + this.j;
        return hash;
    }
    
    @Override
    public String toString() {
        return "("+i+", "+j+")";
    }

    @Override
    public int compareTo(Punto t) {
        if(this.getJ() == t.getJ())
            return Integer.compare(this.getI(), t.getI());
        else
            return Integer.compare(this.getJ(), t.getJ());
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        try {
            
            Punto cloned = (Punto)super.clone();
            return cloned;
        }
        catch(CloneNotSupportedException e) {
            return null;
        }
    }
}