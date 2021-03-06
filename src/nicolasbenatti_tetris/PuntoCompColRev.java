/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

import java.util.Comparator;

/**
 * criterio di ordinamento per indice colonna decrescente
 * @author Nicolas Benatti
 */
public class PuntoCompColRev implements Comparator<Punto> {

    @Override
    public int compare(Punto t, Punto t1) {
        if(t.getJ() == t1.getJ())
            return Integer.compare(t.getI(), t1.getI());
        else
            return Integer.compare(t.getJ(), t1.getJ());
    }
}
