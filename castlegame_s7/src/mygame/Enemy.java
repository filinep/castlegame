/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author scott
 */
public class Enemy extends GameEntity {

    public Enemy(GameLogic gl) {
        super(gl);
        type = TYPE.Enemy;
    }
    
}
