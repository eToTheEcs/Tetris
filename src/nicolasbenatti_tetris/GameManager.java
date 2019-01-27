/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nicolasbenatti_tetris;

/**
 * gestore dei punteggi e dei livelli della partita
 * @author Nicolas Benatti
 */
public class GameManager {
    
    /**
     * punteggio della partita in corso
     */
    private int score;
    
    /**
     * livello raggiunto dal giocatore
     */
    private int level;

    /**
     * no. di righe completate nel livello (max 4).
     */
    private int linesClearedInLevel;
    
    /**
     * valori iniziali dei punteggi nel primo livello<br>
     * dai quali vengono generati tutti gli altri.
     */
    private static int[] scoreVector = new int[]{40, 100, 300, 1200};
    
    public GameManager() {
        linesClearedInLevel = score = level = 0;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public int getLinesClearedInLevel() {
        return linesClearedInLevel;
    }
    
    public void notifyLineClear() {
        
        linesClearedInLevel = (linesClearedInLevel + 1) % 4;
        
        if(linesClearedInLevel == 0)
            levelUp();
    }
    
    /**
     * alza il livello automaticamente ogni 4 righe completate
     */
    private void levelUp() {
        level++;
        
        if(level > 0)
            linesClearedInLevel = 1;    // avoid having a value of 0 when clearing the last line of the level
    }
    
    public void scoreUp() {
        
        score += scoreVector[linesClearedInLevel-1] * (level + 1);
    }
}
