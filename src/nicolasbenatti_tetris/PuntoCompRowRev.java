/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

import java.util.Comparator;

/**
 * criterio di ordinamento per indice riga decrescente <br>
 * e, a parità di riga, per colonna decrescente.
 * @author Nicolas Benatti
 */
public class PuntoCompRowRev implements Comparator<Punto> {

    @Override
    public int compare(Punto t, Punto t1) {
        
        if(t.getI() == t1.getI())
            return Integer.compare(t.getJ(), t1.getJ());
        else
            return Integer.compare(t.getI(), t1.getI());
    }
}
