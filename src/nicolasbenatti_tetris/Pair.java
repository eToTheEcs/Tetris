/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

import java.util.Objects;

/**
 * contenitore in grado di ospitare coppie di oggetti generici
 * @author Nicolas Benatti
 * @param <F> type of the 1st element holded
 * @param <S> type of the 2nd element holded
 */
public class Pair<F extends Cloneable, S extends Cloneable> {
    
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.first);
        hash = 97 * hash + Objects.hashCode(this.second);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.second, other.second)) {
            return false;
        }
        return true;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        try {
            Pair<F, S> cloned = (Pair<F, S>)super.clone();
            cloned.first = (F)this.first;
            cloned.second = (S)this.second;
            return cloned;
        }
        catch(CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "<" + first.toString() + ", " + second.toString() + ">";
    }
}
