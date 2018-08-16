/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

import java.util.Comparator;

/**
 * criterio di ordinamento per indice riga decrescente
 * @author Nicolas Benatti
 */
public class PuntoCompRow implements Comparator<Punto> {

    @Override
    public int compare(Punto t, Punto t1) {
        
        if(t.getI() == t1.getI())
            return Integer.compare(t1.getJ(), t.getJ());
        else
            return Integer.compare(t1.getI(), t.getI());
    }
}
