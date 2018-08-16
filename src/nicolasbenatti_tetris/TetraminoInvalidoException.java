/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

/**
 *
 * @author Nicolas Benatti
 */
public class TetraminoInvalidoException extends Exception {

    /**
     * Creates a new instance of <code>TetraminoInvalidoException</code> without
     * detail message.
     */
    public TetraminoInvalidoException() {
    }

    /**
     * Constructs an instance of <code>TetraminoInvalidoException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public TetraminoInvalidoException(String msg) {
        super(msg);
    }
}
