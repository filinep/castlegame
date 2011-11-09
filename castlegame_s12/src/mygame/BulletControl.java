/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
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
public class BulletControl extends RigidBodyControl
        implements PhysicsCollisionListener {

    private Bullet bullet;
    private boolean markedForDeletion = false;
    private float timeToLive;
    private final static float SCALING_FACTOR = 3f;

    public BulletControl(Bullet bull, CollisionShape shape, float mass, float range) {
        super(shape, mass);
        bullet = bull;
        Main.get().getBulletAppState().getPhysicsSpace().addCollisionListener(this);
        this.timeToLive = range * SCALING_FACTOR;
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
        checkCollision(a, b);
        checkCollision(b, a);
    }
    
    private void checkCollision(Spatial a, Spatial b) {
        if (b == this.spatial) {
            if (a != null) {
                //Delete the bullet no matter what it hits
                System.out.println("Collided with " + a.getName());
                markedForDeletion = true;
            }
        }
    }

    public BulletControl cloneForSpatial(Bullet bull, Spatial spatial) {
        BulletControl control = new BulletControl(bull, collisionShape, mass, timeToLive);
        control.setAngularFactor(getAngularFactor());
        control.setAngularSleepingThreshold(getAngularSleepingThreshold());
        control.setCcdMotionThreshold(getCcdMotionThreshold());
        control.setCcdSweptSphereRadius(getCcdSweptSphereRadius());
        control.setCollideWithGroups(getCollideWithGroups());
        control.setCollisionGroup(getCollisionGroup());
        control.setDamping(getLinearDamping(), getAngularDamping());
        control.setFriction(getFriction());
        control.setGravity(getGravity());
        control.setKinematic(isKinematic());
        control.setKinematicSpatial(isKinematicSpatial());
        control.setLinearSleepingThreshold(getLinearSleepingThreshold());
        control.setPhysicsLocation(getPhysicsLocation(null));
        control.setPhysicsRotation(getPhysicsRotationMatrix(null));
        control.setRestitution(getRestitution());

        if (mass > 0) {
            control.setAngularVelocity(getAngularVelocity());
            control.setLinearVelocity(getLinearVelocity());
        }
        control.setApplyPhysicsLocal(isApplyPhysicsLocal());

        control.setSpatial(spatial);
        return control;
    }

    @Override
    protected void createCollisionShape() {
        super.createCollisionShape();
    }

    @Override
    public PhysicsSpace getPhysicsSpace() {
        return super.getPhysicsSpace();
    }

    @Override
    public boolean isApplyPhysicsLocal() {
        return super.isApplyPhysicsLocal();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public boolean isKinematicSpatial() {
        return super.isKinematicSpatial();
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
        super.render(rm, vp);
    }

    @Override
    public void setApplyPhysicsLocal(boolean applyPhysicsLocal) {
        super.setApplyPhysicsLocal(applyPhysicsLocal);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public void setKinematicSpatial(boolean kinematicSpatial) {
        super.setKinematicSpatial(kinematicSpatial);
    }

    @Override
    public void setPhysicsSpace(PhysicsSpace space) {
        super.setPhysicsSpace(space);
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        timeToLive -= this.getLinearVelocity().length() * tpf;
        if (timeToLive <= 0 || markedForDeletion) {
            Main.get().getRootNode().detachChild(this.spatial);
            if (this != null) {
                space.removeCollisionListener(this);
                space.remove(this);
                bullet.kill();
            }

        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
    }
}
