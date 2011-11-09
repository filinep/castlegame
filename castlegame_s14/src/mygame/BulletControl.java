/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;

/**
 *
 * @author scott
 */
public class BulletControl extends RigidBodyControl
        implements PhysicsCollisionListener {

    private Bullet bullet;
    private boolean markedForDeletion = false;
    private float timeToLive;
    private final static float SCALING_FACTOR = 3f;

    public BulletControl(Bullet bull, CollisionShape shape, float mass, float range) {
        super(shape, mass);
        bullet = bull;
        bullet.getPhysicsSpace().addCollisionListener(this);
        this.timeToLive = range * SCALING_FACTOR;
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
                //Delete the bullet no matter what it hits
                System.out.println("Collided with " + a.getName());
                
                if(a instanceof GameEntity) {
                    ((GameEntity) a).damage(bullet.getDamage());
                }
                
                markedForDeletion = true;
            }
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        timeToLive -= this.getLinearVelocity().length() * tpf;
        if (timeToLive <= 0 || markedForDeletion) {
            if (this != null && space != null) {
                space.removeCollisionListener(this);
                space.remove(this);
                bullet.kill();
            }
        }
    }
}
