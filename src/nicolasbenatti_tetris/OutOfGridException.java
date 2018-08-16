/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

/**
 * lanciata quando si accede ad una cella della griglia al di fuori della sua dimensione.
 * @author Nicolas Benatti
 */
public class OutOfGridException extends RuntimeException {

    /**
     * Creates a new instance of <code>OutOfGridException</code> without detail
     * message.
     */
    public OutOfGridException() {
    }

    /**
     * Constructs an instance of <code>OutOfGridException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public OutOfGridException(String msg) {
        super(msg);
    }
}
