/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

/**
 * categorie di tetramini del gioco
 * @author Nicolas Benatti
 */
public enum TetraminoType {
    I((byte)1),
    O((byte)2),
    T((byte)3),
    S((byte)4),
    Z((byte)5),
    J((byte)6),
    L((byte)7);
    
    /**
     * valore intero della costante
     */
    private final byte val;
    
    
    private TetraminoType(byte val) {
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
