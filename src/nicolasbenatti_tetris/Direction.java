/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

/**
 * direzioni possibili di movimento
 * @author Nicolas Benatti
 */
public enum Direction {
    SX((byte)0),
    DX((byte)1),
    DOWN((byte)2);
    
    /**
     * valore intero della costante
     */
    private final byte val;
    
    
    private Direction(byte val) {
        this.val = val;
    }
    
    /**
     * ritorna il valore intero della costante
     * @return intero associato alla costante
     */
    public byte getValue() {
        return val;
    }
}
