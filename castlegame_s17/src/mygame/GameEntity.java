package mygame;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.scene.Node;

/**
 *
 * @author scott
 */
public class GameEntity extends Node {
    public enum TYPE {Player, Enemy, Bullet, MagicEffect, Item, Other};
            
    public TYPE type;
    
    protected GameLogic game;
    
    public GameEntity(GameLogic gl) {
        game = gl;
    }
    
    public GameEntity(GameEntity other) {
        this.game = other.game;
        this.type = other.type;
    }
    
    public void birth() {
        game.add(this);
        game.getRootNode().attachChild(this);
    }
    
    public void update(float tpf) {}
    public void impact() {}
    public void damage(int dhp) {}
    
    public void kill() {
        game.remove(this);
        game.getRootNode().detachChild(this);
    }
    
    public PhysicsSpace getPhysicsSpace() {
        return game.getPhysicsSpace();
    }
}
