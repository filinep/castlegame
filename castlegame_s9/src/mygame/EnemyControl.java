/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import java.io.IOException;

/**
 *
 * @author scott
 */
public class EnemyControl extends CharacterControl implements PhysicsCollisionListener {

    private Enemy enemy;
    private boolean markedForDeletion = false;
    private float timeToLive;
    private final static float SCALING_FACTOR = 3f;

    public EnemyControl(Enemy enem, CollisionShape shape, float stepHeight) {
        super(shape, stepHeight);
        enemy = enem;
        //this.bullet = bullet;
        //this.timeToLive = timeToLive;
        Main.get().getBulletAppState().getPhysicsSpace().addCollisionListener(this);
        //this.timeToLive = r;
    }

    public EnemyControl() {
        //this.bullet = bullet;
        this.timeToLive = timeToLive;
    }

    public void prePhysicsTick(PhysicsSpace space, float f) {
        // apply state changes ...
    }

    public void physicsTick(PhysicsSpace space, float f) {
        // poll game state ...
    }

    public void collision(PhysicsCollisionEvent event) {
        Spatial a = event.getNodeA();
        Spatial b = event.getNodeB();
        if (a != null) {
            if (a == this.spatial) {
                // node = (Node)event.getNodeA();
                /** ... do something with the node ... */
                if (b != null) {
                    if (b.getName().equals("Bullet")) {

                        System.out.println(b.getName());
                        markedForDeletion = true;
                    }
                }
            }
            if (b != null) {
                if (b == this.spatial) {
                    //Node node = (Node)event.getNodeB();
                    /** ... do something with the node ... */
                    if (a != null) {
                        if (a.getName().equals("Bullet")) {

                            System.out.println(a.getName());
                            markedForDeletion = true;
                        }
                    }
                }
            }
        }
    }

    public void update(float tpf) {
        super.update(tpf);

        //timeToLive -= this.getLinearVelocity().length() * tpf;
        if (markedForDeletion) {
            Main.get().getRootNode().detachChild(this.spatial);
            if (this != null) {
                space.removeCollisionListener(this);
                space.remove(this);
                enemy.kill();
            }

        }
    }
}
