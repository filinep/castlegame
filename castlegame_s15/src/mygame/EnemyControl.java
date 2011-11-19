/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.scene.Spatial;

/**
 *
 * @author scott
 */
public class EnemyControl extends CharacterControl implements PhysicsCollisionListener {

    private Enemy enemy;
    private boolean markedForDeletion = false;

    public EnemyControl(Enemy enemy, CollisionShape shape, float stepHeight) {
        super(shape, stepHeight);
        
        this.enemy = enemy;
        enemy.getPhysicsSpace().addCollisionListener(this);
    }

    public void collision(PhysicsCollisionEvent event) {
        Spatial a = event.getNodeA();
        Spatial b = event.getNodeB();
        checkCollision(a, b);
        checkCollision(b, a);
    }
    
    private void checkCollision(Spatial a, Spatial b) {
        if (b == this.spatial && !markedForDeletion) {
            if (a != null) {
                if (a.getName().toLowerCase().contains("bullet")) {
                    markedForDeletion = true;
                }
            }
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        if (markedForDeletion) {
            if (this != null && space != null) {
                space.removeCollisionListener(this);
                space.remove(this);
                enemy.kill();
            }
        }
    }
}
