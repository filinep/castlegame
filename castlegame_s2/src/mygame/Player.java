/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author filipe
 */
public class Player extends Node {
    private PhysicsCharacter physicsControl;
    private Spatial position;
    
    private Weapon activeWeapon;
    
    public Player(Spatial startLoc) {
        this.physicsControl = new PhysicsCharacter(new CapsuleCollisionShape(1f, 5f), .01f);
        this.physicsControl.setJumpSpeed(20);
        this.physicsControl.setFallSpeed(30);
        this.physicsControl.setGravity(30);
        this.physicsControl.setPhysicsLocation(startLoc.getLocalTranslation());
        
        this.position = startLoc;
        this.activeWeapon = Weapon.RANGED;
    }
    
    public void useWeapon() {
        activeWeapon.fire();
    }

    public void setPosition(Spatial position) {
        this.position = position;
    }

    public Spatial getPosition() {
        return position;
    }

    public PhysicsCharacter getPhysicsControl() {
        return physicsControl;
    }
}
