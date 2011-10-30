/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

/**
 *
 * @author filipe
 */
public class Player implements ActionListener {
    private PhysicsCharacter physicsControl;
    private boolean left = false, right = false, up = false, down = false;
    private Weapon activeWeapon;
    private Vector3f walkDirection;
    
    public Player(Spatial startLoc) {
        this.physicsControl = new PhysicsCharacter(new CapsuleCollisionShape(1f, 5f), .01f);
        this.physicsControl.setJumpSpeed(20);
        this.physicsControl.setFallSpeed(30);
        this.physicsControl.setGravity(30);
        this.physicsControl.setPhysicsLocation(startLoc.getLocalTranslation());
        
        this.walkDirection = new Vector3f();
        this.activeWeapon = Weapon.RANGED;
    }
    
    public void useWeapon() {
        Camera camera = Main.get().getCamera();
        Vector3f from = camera.getLocation().add(camera.getDirection());
        Vector3f to = camera.getLocation().add(camera.getDirection().mult(2f));
        
        activeWeapon.fire(from, to);
    }

    public PhysicsCharacter getPhysicsControl() {
        return physicsControl;
    }
    
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Lefts")) {
            left = value;
        } else if (binding.equals("Rights")) {
            right = value;
        } else if (binding.equals("Ups")) {
            up = value;
        } else if (binding.equals("Downs")) {
            down = value;
        } else if (binding.equals("Space")) {
            physicsControl.jump();
        }
        
        if (binding.equals("Fire") && value) {
            useWeapon();
        }
    }
    
    public void update(float tpf) {
        Camera camera = Main.get().getCamera();
        Vector3f camDir = camera.getDirection().clone().multLocal(.6f);
        Vector3f camLeft = camera.getLeft().clone().multLocal(.4f);
        walkDirection.set(0, 0, 0);
        
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }

        physicsControl.setWalkDirection(walkDirection);
        camera.setLocation(physicsControl.getPhysicsLocation());
    }
}
