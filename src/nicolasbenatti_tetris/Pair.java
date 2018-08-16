/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

/**
 * contenitore in grado di ospitare coppie di oggetti generici
 * @author Nicolas Benatti
 */
public class Pair<F, S> {
    
    private F first;
    private S second;
    
    public Pair(F f, S s) {
        
        first = f;
        second = s;
    }

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }
}
