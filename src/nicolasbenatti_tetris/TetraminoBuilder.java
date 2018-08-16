/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

/**
 * contiene tutte le maschere per disegnare i tetramini
 * @author Nicolas Benatti
 */
public class TetraminoBuilder {
    
    /**
     * dimensione bounding box dell'ultimo tetramino
     * creato (non è pericoloso in quanto non vengono mai eseguite<br>
     * creazioni concorrenti)
     */
    private static int T_DIM;
    
    /**
     * maschere per disegnare i tetramini:<br>
     * l'ultima cella dell'array indica la dimensione della bounding box
     */
    
    private static final Punto[] I_MASK = {
        new Punto(1, 0),
        new Punto(1, 1),
        new Punto(1, 2),
        new Punto(1, 3),
        //new Punto(4, 4)
    };
    
    private static final Punto[] O_MASK = {
        new Punto(0, 0),
        new Punto(0, 1),
        new Punto(1, 0),
        new Punto(1, 1),
        //new Punto(3, 3) // poco utile per questo tetramino, in quanto non viene mai ruotato
    };
    
    private static final Punto[] T_MASK = {
        new Punto(0, 1),
        new Punto(1, 0),
        new Punto(1, 1),
        new Punto(1, 2),
        //new Punto(3, 3)
    };
    
    private static final Punto[] S_MASK = {
        new Punto(0, 1),
        new Punto(0, 2),
        new Punto(1, 0),
        new Punto(1, 1),
        //new Punto(3, 3)
    };
    
    private static final Punto[] Z_MASK = {
        new Punto(0, 0),
        new Punto(0, 1),
        new Punto(1, 1),
        new Punto(1, 2),
        //new Punto(3, 3)
    };
    
    private static final Punto[] J_MASK = {
        new Punto(0, 0),
        new Punto(1, 0),
        new Punto(1, 1),
        new Punto(1, 2),
        //new Punto(3, 3)
    };
    
    private static final Punto[] L_MASK = {
        new Punto(0, 2),
        new Punto(1, 0),
        new Punto(1, 1),
        new Punto(1, 2),
        //new Punto(3, 3)
    };
    
    /**
     * costruisci un tetramino del tipo scelto
     * @param tt tipo del tetramino da costruire
     * @return matrice contenente il tetramino scelto.
     */
    public static byte[][] build(TetraminoType tt) {
        
        // il tetramino I è racchiuso in una 4x4, tutti gli altri tetramini in una 3x3
        T_DIM = (tt.equals(TetraminoType.I)) ? 4 : 3; 
       
        byte[][] tetraminoMatrix = new byte[T_DIM][T_DIM];
        
        for(int i = 0; i < T_DIM; ++i)
            for(int j = 0; j < T_DIM; ++j)
                tetraminoMatrix[i][j] = 0;
        
        switch(tt) {
            
            case I:
                
                for(Punto c : I_MASK) {
                    tetraminoMatrix[c.getI()][c.getJ()] = TetraminoType.I.getValue();
                }
                
                break;
            case O:
                
                for(Punto c : O_MASK) {
                    tetraminoMatrix[c.getI()][c.getJ()] = TetraminoType.O.getValue();
                }
                break;
            case T:
                
                for(Punto c : T_MASK) {
                    tetraminoMatrix[c.getI()][c.getJ()] = TetraminoType.T.getValue();
                }
                break;
            case S:
                
                for(Punto c : S_MASK) {
                    tetraminoMatrix[c.getI()][c.getJ()] = TetraminoType.S.getValue();
                }
                break;
            case Z:
                
                for(Punto c : Z_MASK) {
                    tetraminoMatrix[c.getI()][c.getJ()] = TetraminoType.Z.getValue();
                }
                break;
            case J:
                
                for(Punto c : J_MASK) {
                    tetraminoMatrix[c.getI()][c.getJ()] = TetraminoType.J.getValue();
                }
                break;
            case L:
                
                for(Punto c : L_MASK) {
                    tetraminoMatrix[c.getI()][c.getJ()] = TetraminoType.L.getValue();
                }
                break;
        }
        
        return tetraminoMatrix;
    }
}
