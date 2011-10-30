/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
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
        Camera camera = Main.get().getCamera();
        Vector3f from = camera.getLocation().add(camera.getDirection());
        Vector3f to = camera.getLocation().add(camera.getDirection().mult(2f));
        
        activeWeapon.fire(from, to);
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
