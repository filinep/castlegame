/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.PhysicsSpace;

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
    
    public void update(float tpf) {}
    public void impact() {}
    public void damage(int dhp) {}
    public void kill() {
        game.remove(this);
    }
    
    public PhysicsSpace getPhysicsSpace() {
        return game.getPhysicsSpace();
    }
}
