/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author scott
 */
public class GameEntity {
    public enum TYPE {Player, Enemy, Bullet, MagicEffect, Item, HUD};
            
    public TYPE type;
    
    protected GameLogic game;
    
    public GameEntity(GameLogic gl) {
        game = gl;
    }
    
    public void birth() {
        game.add(this);
    }
    
    public void update() {}
    public void impact() {}
    public void damage(int dhp) {}
    public void kill() {
        game.remove(this);
    }
    
}
